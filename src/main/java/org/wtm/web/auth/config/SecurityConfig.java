package org.wtm.web.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.wtm.web.auth.handler.CustomSuccessHandler;
import org.wtm.web.auth.jwt.JWTFilter;
import org.wtm.web.auth.utils.JWTUtil;
import org.wtm.web.auth.jwt.LoginFilter;
import org.wtm.web.auth.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//    http
//        .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
//
//          @Override
//          public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//
//            CorsConfiguration configuration = new CorsConfiguration();
//
//            configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
//            configuration.setAllowedMethods(Collections.singletonList("*"));
//            configuration.setAllowCredentials(true);
//            configuration.setAllowedHeaders(Collections.singletonList("*"));
//            configuration.setMaxAge(3600L);
//
//            // 백엔드에서 정보를 제공할 때 웹페이지에서 보여질 수 있도록 하려면 setExposedHeaders 설정
//            configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
//            configuration.setExposedHeaders(Collections.singletonList("Authorization"));
//
//            return configuration;
//          }
//        }));
//
//    //csrf disable
//    http
//        .csrf((auth) -> auth.disable());
//
//    //From 로그인 방식 disable
//    http
//        .formLogin((auth) -> auth.disable());
//
//    //HTTP Basic 인증 방식 disable
//    http
//        .httpBasic((auth) -> auth.disable());
//
//    //필터 추가 LoginFilter()는 인자를 받음 (AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함) 따라서 등록 필요
//    http
//        .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
//
//    //    // JWTFilter 추가
//    //    http
//    //        .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
//    // jwt 기한 만료시 재로그인할 때, 무한루프 방지를 위한 JWTFilter 위치 변경
////    http
////        .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);
//
//    //oauth2
//    http
//        .oauth2Login((oauth2) -> oauth2
//            .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
//                .userService(customOAuth2UserService))
//            .successHandler(customSuccessHandler)
//        );
//
//    //경로별 인가 작업
//    http
//        .authorizeHttpRequests((auth) -> auth
//            .requestMatchers("/", "/login","/signUp", "/user").permitAll()
//            .requestMatchers("/admin").hasRole("ADMIN")
//            .anyRequest().authenticated());
//
//    http
//        .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
//        })
//    );
//
//    //세션 설정 : STATELESS
//    http
//        .sessionManagement((session) -> session
//            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//    return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CORS 설정
        http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setMaxAge(3600L);
                configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                return configuration;
            }
        }));

        // CSRF 및 기본 로그인 방식 비활성화
        http.csrf((csrf) -> csrf.disable());
        http.formLogin((form) -> form.disable());
        http.httpBasic((basic) -> basic.disable());

        // 경로별 인가 설정
        http.authorizeHttpRequests((auth) -> auth
            .requestMatchers("/", "/login", "/signUp", "/user").permitAll()
            .requestMatchers("/admin").hasRole("ADMIN")
            .anyRequest().authenticated());

        // 일반 로그인용 LoginFilter 추가
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
            .successHandler(customSuccessHandler)
        );

        // 통합 JWTFilter 추가 - 일반 로그인과 OAuth2 로그인 모두 처리
        http.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 예외 처리 핸들러
        http.exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint((request, response, authException) -> {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            })
        );

        // 세션 정책: STATELESS
        http.sessionManagement((session) -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}