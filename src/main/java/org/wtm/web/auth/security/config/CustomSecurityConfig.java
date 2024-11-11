package org.wtm.web.auth.security.config;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.wtm.web.auth.security.APIUserDetailsService;
import org.wtm.web.auth.security.filter.APILoginFilter;
import org.wtm.web.auth.security.filter.RefreshTokenFilter;
import org.wtm.web.auth.security.filter.TokenCheckFilter;
import org.wtm.web.auth.security.handler.APILoginSuccessHandler;
import org.wtm.web.auth.security.handler.CustomSocialLoginSuccessHandler;
import org.wtm.web.auth.security.handler.OAuth2SuccessHandler;
import org.wtm.web.auth.util.JWTUtil;
import org.wtm.web.user.constants.UserRole;

@Log4j2
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class CustomSecurityConfig {

    private final APIUserDetailsService apiUserDetailsService;
    private final JWTUtil jwtUtil;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Nuxt 서버의 주소와 포트를 명확하게 설정
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,  @Qualifier("corsConfigurationSource") CorsConfigurationSource corsConfigurationSource) throws Exception {

        // AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(
            AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
            .userDetailsService(apiUserDetailsService)
            .passwordEncoder(passwordEncoder());

        // Get AuthenticationManager
        AuthenticationManager authenticationManager =
            authenticationManagerBuilder.build();
        
        // 반드시 필요
        http.authenticationManager(authenticationManager);

        // API LoginFilter
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken"); // TODO: login 엔드포인트로 변경
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        // API LoginSuccessHandler
        APILoginSuccessHandler successHandler = new APILoginSuccessHandler(jwtUtil);
        // SuccessHandler 세팅
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

        //APILoginFilter 위치 조정
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

        // api로 시작하는 모든 경로는 TokenCheckFilter 동작
        http.addFilterBefore(
            tokenCheckFilter(apiUserDetailsService, jwtUtil),
            UsernamePasswordAuthenticationFilter.class);

        // refreshToken 호출 처리
        http.addFilterBefore(new RefreshTokenFilter("/refreshToken", jwtUtil), TokenCheckFilter.class);

        log.info("----------------configure--------------------");
        // 허용할 엔드포인트 설정
        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/generateToken", "/refreshToken").permitAll() // 비인증 사용자가 접근할 수 있는 경로
            .requestMatchers("/", "api/v1/auth/**", "/oauth2/**").permitAll()
            .requestMatchers("/api/v1/admin/**").hasRole(String.valueOf(UserRole.ADMIN))
            .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**").permitAll() // 정적 리소스에 대해 permitAll 설정
            .anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
        );

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
//            .successHandler(authenticationSuccessHandler()) // OAuth2 로그인 성공 시 처리 핸들러
            .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
            .userInfoEndpoint(endpoint -> endpoint.userService(oAuth2UserService))
            .successHandler(oAuth2SuccessHandler)
        );


        http.formLogin(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable) // csrf 토큰 비활성화
            .cors(cors->cors.configurationSource(corsConfigurationSource))
            .sessionManagement(session-> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    private TokenCheckFilter tokenCheckFilter(APIUserDetailsService apiUserDetailsService, JWTUtil jwtUtil) {
        return new TokenCheckFilter(apiUserDetailsService, jwtUtil);
    }
}
