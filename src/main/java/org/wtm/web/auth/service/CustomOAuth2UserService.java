package org.wtm.web.auth.service;

import io.jsonwebtoken.security.Password;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.Delimiter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.wtm.web.auth.dto.GoogleResponse;
import org.wtm.web.auth.dto.KakaoResponse;
import org.wtm.web.auth.dto.NaverResponse;
import org.wtm.web.auth.dto.OAuth2Response;
import org.wtm.web.auth.dto.UserDto;
import org.wtm.web.auth.dto.CustomOAuth2User;
import org.wtm.web.common.repository.UserRepository;
import org.wtm.web.user.constants.UserRole;
import org.wtm.web.user.model.User;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User oAuth2User = super.loadUser(userRequest);

    System.out.println(oAuth2User);
    Map<String, Object> attributes = oAuth2User.getAttributes();

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    OAuth2Response oAuth2Response = null;
    if (registrationId.equals("naver")) {
      oAuth2Response = new NaverResponse(attributes);
    } else if (registrationId.equals("google")) {
      oAuth2Response = new GoogleResponse(attributes);
    } else if (registrationId.equals("kakao")) {
      oAuth2Response = new KakaoResponse(attributes);
    } else {
      return null;
    }

    //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듦
    String username = oAuth2Response.getProvider()+"_"+oAuth2Response.getProviderId();
    String email = oAuth2Response.getEmail();

    Optional<User> optionalUser = userRepository.findByEmail(email);
    // 원시 속성 정보


    if(optionalUser.isEmpty()) {

      User user = User.builder()
          .email(oAuth2Response.getEmail())
          .username(username)
          .password(passwordEncoder.encode("1111"))
          .name(oAuth2Response.getName())
          .role(UserRole.USER)
          .social(true)
          .build();

      userRepository.save(user);

      UserDto userDto = new UserDto();
      userDto.setUsername(email);
      userDto.setName(oAuth2Response.getName());
      userDto.setRole(String.valueOf(UserRole.USER));

      return new CustomOAuth2User(userDto, attributes);
    } else {

      User user = optionalUser.get();
      user.updateEmail(oAuth2Response.getEmail());
      user.updateName(oAuth2Response.getName());

      userRepository.save(user);

      UserDto userDto = new UserDto();
      userDto.setUsername(user.getEmail());
      userDto.setName(oAuth2Response.getName());
      userDto.setRole(String.valueOf(user.getRole()));

      return new CustomOAuth2User(userDto, attributes);
    }
  }
}