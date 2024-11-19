package org.wtm.web.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.wtm.web.auth.handler.CustomAuthenticationEntryPoint;
import org.wtm.web.auth.handler.CustomOAuth2FailureHandler;
import org.wtm.web.auth.handler.CustomOAuth2SuccessHandler;
import org.wtm.web.auth.jwt.JWTFilter;
import org.wtm.web.auth.jwt.LoginFilter;
import org.wtm.web.auth.service.AuthService;
import org.wtm.web.auth.service.CustomOAuth2UserService;
import org.wtm.web.auth.utils.JWTUtil;

@Configuration
@EnableWebSecurity(debug = true)
//@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthService authService;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

    private final JWTUtil jwtUtil;

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                configuration.setAllowedMethods(
                    Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
                configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
                configuration.setMaxAge(3600L); // 캐시 유효 시간 1시간 설정
                return configuration;
            }
        }));

        // CSRF 및 기본 로그인 방식 비활성화
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 경로별 인가 설정
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/api/v1/auth/me").authenticated()
                .anyRequest().permitAll())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(customAuthenticationEntryPoint)); // Custom EntryPoint 추가


//            .requestMatchers("/",
//                "/api/v1/auth/user/signUp",
//                "/api/v1/auth/admin/signUp",
//                "/api/v1/*",
//                "/api/v1/auth/check-email").permitAll());
//            .requestMatchers(ApiPaths.ADMIN + "/**").hasRole("ADMIN")
//            .anyRequest().authenticated());

        // 일반 로그인용 LoginFilter 추가
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setFilterProcessesUrl("/api/v1/auth/signIn"); // 로그인 경로 설정
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
            .successHandler(customOAuth2SuccessHandler)
            .failureHandler(customOAuth2FailureHandler) // 실패 시 핸들러 추가
        );

        // 통합 JWTFilter 추가 - 일반 로그인과 OAuth2 로그인 모두 처리
        http.addFilterBefore(new JWTFilter(authService, jwtUtil), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterAfter(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

//        // 예외 처리 핸들러
//        http.exceptionHandling(exceptionHandling -> exceptionHandling
//            .authenticationEntryPoint((request, response, authException) -> {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//            })
//        );

        // 세션 정책: STATELESS
        http.sessionManagement((session) -> session
//            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)); // OAuth2 로그인에는 session이 필요
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}