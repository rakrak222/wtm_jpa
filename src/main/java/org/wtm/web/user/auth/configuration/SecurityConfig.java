package org.wtm.web.user.auth.configuration;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("dev") // 'test' 프로파일 활성화 시 적용
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 : 테스트할 때만
            .authorizeHttpRequests(auth -> auth  // Customizer로 authorizeHttpRequests 설정
                    .requestMatchers("/api/v1/user/login",
                                     "/api/v1/user/signup",
                                     "/error").permitAll() // 로그인 엔드포인트 접근 허용
                    .requestMatchers("/api/admin/**").hasRole("ADMIN") // hasRole 메서드는 역할 이름에서 ROLE_ 접두사를 생략, Spring Security에서 자동으로 접두사 추가
//                    .requestMatchers("/api/user/**").hasRole("USER") // TODO : USER인 경우에 들어갈 수 있는 인증 권한 확인 필요
                                .anyRequest().authenticated() // 다른 요청은 인증 필요
            ).logout(logout -> logout
                .logoutUrl("/api/v1/user/logout")  // 로그아웃 URL 설정
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK); // 로그아웃 성공 시 상태 코드 설정
                    response.getWriter().write("Logout successful"); // 로그아웃 성공 메시지
                    response.getWriter().flush();
                })
                .invalidateHttpSession(true)  // 세션 무효화
                .deleteCookies("JSESSIONID")  // 로그아웃 시 삭제할 쿠키 설정
            ).exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(
                    (request, response, authException) -> // 인증 실패 시 응답 설정
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                            "Unauthorized: Please check your credentials"))
                .accessDeniedHandler((request, response, accessDeniedException) -> // 접근 거부 시 응답 설정
                    response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Access Denied: You do not have permission")));

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}