package org.wtm.web.auth.service;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.wtm.web.auth.dto.UserSecurityDto;
import org.wtm.web.common.repository.UserRepository;
import org.wtm.web.user.constants.UserRole;
import org.wtm.web.user.model.User;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("userRequest...");
    log.info(userRequest);

    log.info("oauth2 user........................................");

    ClientRegistration clientRegistration = userRequest.getClientRegistration();
    String clientName = clientRegistration.getClientName();

    log.info("NAME: {}", clientName);

    OAuth2User oAuth2User = super.loadUser(userRequest);
    Map<String, Object> paramMap = oAuth2User.getAttributes();

    Map<String, String> kakaoUserInfo = null;

    // Oauth2 추가할 서비스 있다면 더 추가
    switch (clientName) {
      case "kakao":
        kakaoUserInfo = getKakaoUserInfo(paramMap);
        break;
    }

    log.info("=============================================");
    log.info("email: {}", kakaoUserInfo.get("email"));
    log.info("nickname: {}", kakaoUserInfo.get("nickname"));
    log.info("=============================================");

    return generateDto(kakaoUserInfo, paramMap);
  }
  // 이미 회원가입된 회원은 기존 정보 반환, 새로운 사용자는 자동으로 회원 가입 처리
  private UserSecurityDto generateDto(Map<String, String> kakakoUserInfo, Map<String, Object> params) {

    String email = kakakoUserInfo.get("email");
    String nickname = kakakoUserInfo.get("nickname");

    Optional<User> result = userRepository.findByEmail(email);

    // DB에 해당 이메일 사용자가 없다면
    if (result.isEmpty()) {
      // 회원 추가 --
      User user = User.builder()
          .email(email)
          .password(passwordEncoder.encode("1111"))
          .name(nickname)
          .social(true)
          .role(UserRole.USER)
          .build();

      userRepository.save(user);

      //UserSecurityDto 구성 및 반환
      UserSecurityDto userSecurityDto = new UserSecurityDto(email, "1111", nickname, true, List.of(new SimpleGrantedAuthority("ROLE_USER")));
      userSecurityDto.setProps(params);

      return userSecurityDto;

    } else {

      User user = result.get();
      return new UserSecurityDto(
              user.getEmail(),
              user.getPassword(),
              user.getName(),
              user.isSocial(),
              user.getAuthorities());
    }
  }


  private Map<String, String> getKakaoUserInfo(Map<String, Object> paramMap){

    log.info("KAKAO-----------------------------------");

    Object value = paramMap.get("kakao_account");

    log.info(value);

    LinkedHashMap accountMap = (LinkedHashMap) value;

    String email = (String) accountMap.get("email");

    // profile 객체 가져오기
    Object profileObject = accountMap.get("profile");
    LinkedHashMap profileMap = (LinkedHashMap) profileObject;

    // nickname 값 가져오기
    String nickname = (String) profileMap.get("nickname");

    log.info("email..." + email);
    log.info("nickname: " + nickname);

    Map<String, String> kakaoUserInfo = new HashMap<>();
    kakaoUserInfo.put("email", email);
    kakaoUserInfo.put("nickname", nickname);

    return kakaoUserInfo;
  }

}

