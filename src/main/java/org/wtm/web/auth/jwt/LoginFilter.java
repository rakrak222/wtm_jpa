package org.wtm.web.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.wtm.web.auth.dto.CustomUserDetails;
import org.wtm.web.auth.dto.LoginRequestDto;
import org.wtm.web.auth.utils.JWTUtil;

@Log4j2
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    // authentication 객체를 검증하는 역할
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    // 로그인 인증
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginRequestDto loginRequestDto = null;

        try {
            // JSON 요청 본문을 LoginRequest 객체로 변환
            ObjectMapper mapper = new ObjectMapper();
            loginRequestDto = mapper.readValue(request.getInputStream(), LoginRequestDto.class);

        } catch (IOException e) {
            log.error("Failed to read JSON request", e);
            throw new AuthenticationException("Failed to read JSON request", e) {};
        }

        // 추출한 username과 password를 사용해 AuthenticationToken 생성
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();
        String role = loginRequestDto.getRole();

        log.info("Attempting authentication for username: {}, role: {}", username, role);

        if (username == null || password == null || role == null) {
            throw new AuthenticationException("Username, password, or role is missing") {};
        }

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token(dto)에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        Authentication authentication = authenticationManager.authenticate(authToken);

        // Verify role after successful authentication
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String dbRole = customUserDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElseThrow(() -> new AuthenticationException("User role is mismatched") {});

        // Role mismatch check
        if (!role.equals(dbRole)) {
            log.warn("Role mismatch for user: {}. Requested: {}, Actual: {}", username, role, dbRole);
            throw new AuthenticationException("사용자 유형(손님, 사장님)을 확인하세요.") {};
        }

        return authentication;
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
        throws IOException {

        Long expirationMs = 60*60*60*1000L;
        //UserDetailsS
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        Long userId = customUserDetails.getUser().getId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(username, role, expirationMs);

        // JWT 토큰을 쿠키로 전달 (Set-Cookie 헤더 직접 설정)
        ResponseCookie cookie = ResponseCookie.from("Authorization", token)
            .httpOnly(true)
            .secure(false) // HTTPS 환경에서는 true로 설정
            .path("/")
            .domain("localhost")
            .maxAge(expirationMs / 1000) // 초 단위로 변환
            .sameSite("Lax") // 필요에 따라 None, Lax, Strict 설정
            .build();

        // 쿠키를 응답 헤더에 추가
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // JSON 응답 작성
        Map<String, String> body = new HashMap<>();
        body.put("message", "Login successful");
        body.put("username", username);
        body.put("role", role);
        body.put("userId", String.valueOf(userId));

        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.getWriter().flush();

        log.info("Authentication success for user: {}, role: {}", username, role);
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
        throws IOException {

        //로그인 실패시 401 응답 코드 반환
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String, String> body = new HashMap<>();
        body.put("error", "Authentication failed");
        body.put("message", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.getWriter().flush();

        log.info("Authentication failed: {}", failed.getMessage());
    }
}