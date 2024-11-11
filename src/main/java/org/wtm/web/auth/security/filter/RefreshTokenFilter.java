package org.wtm.web.auth.security.filter;

import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.wtm.web.auth.security.exception.RefreshTokenException;
import org.wtm.web.auth.security.exception.RefreshTokenException.ErrorCase;
import org.wtm.web.auth.util.JWTUtil;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshPath;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // refreshPath가 아닌 다른 경로 요청이 들어올 경우 필터를 통과시키고 종료
        if (!path.equals(refreshPath)) {
            log.info("skip refresh token filter......");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Executing RefreshTokenFilter for refresh token path...");

        // 요청 본문에서 accessToken과 refreshToken을 추출
        Map<String, String> tokens = parseRequestJSON(request);
        if (tokens == null) {
            log.error("Token parsing failed. Invalid JSON format.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request format.");
            return;
        }

        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");

        // Access Token 유효성 검사
        try {
            checkAccessToken(accessToken);
        } catch (RefreshTokenException refreshTokenException) {
            refreshTokenException.sendResponseError(response);
            return; // 예외 발생 시 종료
        }

        Map<String, Object> refreshClaims;

        // Refresh Token 유효성 검사
        try {
            refreshClaims = checkRefreshToken(refreshToken);
            log.info("Refresh Token claims: {}", refreshClaims);
        } catch (RefreshTokenException refreshTokenException) {
            refreshTokenException.sendResponseError(response);
            return; // 예외 발생 시 종료
        }

        // 새로운 Access Token과 필요 시 Refresh Token 발행
        String newAccessToken = generateAccessToken(refreshClaims);
        String newRefreshToken = generateRefreshTokenIfNeeded(refreshClaims, refreshToken);

        // 최종적으로 발행된 토큰들을 클라이언트에게 전송
        sendTokens(newAccessToken, newRefreshToken, response);
    }

    // 클라이언트로 토큰 전송
    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(Map.of("accessToken", accessTokenValue, "refreshToken", refreshTokenValue));
        try {
            response.getWriter().println(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 요청 본문에서 JSON 파싱하여 토큰 추출
    private Map<String, String> parseRequestJSON(HttpServletRequest request) {
        try (Reader reader = new InputStreamReader(request.getInputStream())) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    // Access Token 유효성 확인
    private void checkAccessToken(String accessToken) throws RefreshTokenException {
        try {
            jwtUtil.validateToken(accessToken);
        } catch (ExpiredJwtException expiredJwtException) {
            log.info("Access Token has expired");
        } catch (Exception exception) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_ACCESS);
        }
    }

    // Refresh Token 유효성 확인
    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException {
        try {
            return jwtUtil.validateToken(refreshToken);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new RefreshTokenException(ErrorCase.OLD_REFRESH);
        } catch (MalformedJwtException malformedJwtException) {
            log.error("MalformedJwtException-----------------------");
            throw new RefreshTokenException(ErrorCase.NO_REFRESH);
        } catch (Exception exception) {
            throw new RefreshTokenException(ErrorCase.NO_REFRESH);
        }
    }

    // 새로운 Access Token 생성
    private String generateAccessToken(Map<String, Object> refreshClaims) {
        String username = (String) refreshClaims.get("username");
        return jwtUtil.generateToken(Map.of("username", username), JWTUtil.ACCESS_TOKEN_EXPIRATION_TIME);
    }

    // 남은 유효 시간이 짧을 경우 새 Refresh Token 생성
    private String generateRefreshTokenIfNeeded(Map<String, Object> refreshClaims, String oldRefreshToken) {
        Long exp = (Long) refreshClaims.get("exp");
        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
        Date current = new Date(System.currentTimeMillis());
        long gapTime = expTime.getTime() - current.getTime();

        // 유효 기간이 3일 이하인 경우 새 Refresh Token 생성
        if (gapTime < (1000 * 60 * 60 * 24 * 3)) {
            log.info("new Refresh Token required...");
            String username = (String) refreshClaims.get("username");
            return jwtUtil.generateToken(Map.of("username", username), JWTUtil.REFRESH_TOKEN_EXPIRATION_TIME);
        }
        return oldRefreshToken;
    }
}
