package org.wtm.web.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.wtm.web.auth.dto.CustomOAuth2User;
import org.wtm.web.auth.dto.CustomUserDetails;
import org.wtm.web.auth.dto.UserDto;
import org.wtm.web.auth.service.AuthService;
import org.wtm.web.auth.utils.JWTUtil;
import org.wtm.web.common.repository.UserRepository;
import org.wtm.web.user.constants.UserRole;
import org.wtm.web.user.model.User;

@Log4j2
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);

        // 토큰이 없거나 만료된 경우 처리
        if (token == null) {
            log.info("No JWT token found in request.");
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtUtil.isTokenExpired(token)) {
            log.info("JWT token is expired.");

            // 만료된 토큰 쿠키 삭제
            // JWT 토큰을 쿠키로 전달 (Set-Cookie 헤더 직접 설정)
            ResponseCookie expiredCookie = ResponseCookie.from("Authorization", token)
                .httpOnly(true)
                .secure(false) // HTTPS 환경에서는 true로 설정
                .path("/")
                .domain("localhost")
                .maxAge(0) // 즉시 만료
                .sameSite("Lax") // 필요에 따라 None, Lax, Strict 설정
                .build();

            // 쿠키를 응답 헤더에 추가
            response.setHeader(HttpHeaders.SET_COOKIE, expiredCookie.toString());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT token expired. Please re-authenticate.");
            return;
        }

        try {
            // JWT 토큰에서 사용자 정보 추출
            String username = jwtUtil.getUsername(token);

            // User 엔티티 조회
            Optional<User> userOptional = authService.getUserByUsername(username);

            if (userOptional.isEmpty()) {
                log.error("User not found for username: {}", username);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("User not found.");
                return;
            }
            User user = userOptional.get();
            Authentication authToken;
            if (user.isSocial()) {
                // 소셜 로그인 사용자
                log.info("User is a social login user.");

                // 속성 초기화
                Map<String, Object> attributes = Map.of(
                    "username", user.getEmail(),
                    "name", user.getName(),
                    "role", user.getRole().getRole()
                );

                CustomOAuth2User customOAuth2User = new CustomOAuth2User(
                    new UserDto(user.getEmail(), user.getName(), user.getRole().getRole()), attributes);
                authToken = new UsernamePasswordAuthenticationToken(
                    customOAuth2User, null, customOAuth2User.getAuthorities());
            } else {
                // 일반 로그인 사용자

                log.info("User is a standard login user.");
                authToken = new UsernamePasswordAuthenticationToken(
                    new CustomUserDetails(user), null, user.getAuthorities());
            }
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            log.error("Error during JWT authentication: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT token.");
            return;
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // 1. 쿠키에서 토큰 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    log.info("JWT token found in cookie.");
                    return cookie.getValue();
                }
            }
        }

        // 2. 헤더에서 Bearer 토큰 가져오기
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            log.info("JWT token found in Authorization header.");
            return authorizationHeader;
        }
        return null; // 토큰이 없으면 null 반환
    }

}