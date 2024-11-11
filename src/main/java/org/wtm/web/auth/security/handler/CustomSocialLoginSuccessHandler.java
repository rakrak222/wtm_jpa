package org.wtm.web.auth.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.wtm.web.auth.dto.UserSecurityDto;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

  private final PasswordEncoder passwordEncoder;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    log.info("-----------------------------------------------------------------");
    log.info("CustomLoginSuccessHandler onAuthenticationSuccess ...............");
    log.info(authentication.getPrincipal().toString());

    UserSecurityDto userSecurityDto = (UserSecurityDto) authentication.getPrincipal();

    String encodedPassword = userSecurityDto.getPassword();
    
    // 소셜 로그인, password 고정
    if(userSecurityDto.isSocial() && (userSecurityDto.getPassword().equals("1111") || passwordEncoder.matches("1111",
      userSecurityDto.getPassword()))) {
      log.info("Should Change Password");

      log.info("Redirect to User Setting");
      response.sendRedirect("/user/settings"); // TODO : 소셜 가입 시 자동 회원가입할 경우, 패스워드 1111이면 비밀번호 setting 다시 setting하도록 함

    } else {
      log.info("gg"); // TODO: 소셜인증이 아니거나?
    }

  }
}
