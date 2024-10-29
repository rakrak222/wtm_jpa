package org.wtm.web.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wtm.web.common.repository.UserRepository;
import org.wtm.web.user.auth.service.CustomUserDetailsService;
import org.wtm.web.user.auth.utils.TokenProvider;
import org.wtm.web.user.dto.LoginRequestDto;
import org.wtm.web.user.dto.SignUpUserDto;
import org.wtm.web.user.model.User;
import org.wtm.web.user.exceptions.InvalidCredentialsException;
import org.wtm.web.user.exceptions.UserNotFoundException;
import org.wtm.web.user.service.UserService;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public String login(LoginRequestDto loginRequestDto){

        String email = loginRequestDto.getEmail();

        // 사용자 정보 확인
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("%s은 회원가입 정보가 없는 이메일입니다.", email)));

        // password 검증
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException();
        }

        // JWT token 생성
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );

        return tokenProvider.createToken(authentication);
    }

    @Override
    public void signup(SignUpUserDto signUpUserDto) {
        // email 중복 검사
        if (userRepository.existsByEmail(signUpUserDto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        // 비밀번호 암호화 후 User 엔티티 생성 및 저장
        String encodedPassword = passwordEncoder.encode(signUpUserDto.getPassword());
        User user = signUpUserDto.toEntity(encodedPassword);
        userRepository.save(user);
    }
}
