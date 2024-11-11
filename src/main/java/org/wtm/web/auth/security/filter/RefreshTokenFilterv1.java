//package org.wtm.web.auth.security.filter;
//
//import com.google.gson.Gson;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.MalformedJwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.time.Instant;
//import java.util.Date;
//import java.util.Map;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.MediaType;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.wtm.web.auth.security.exception.RefreshTokenException;
//import org.wtm.web.auth.security.exception.RefreshTokenException.ErrorCase;
//import org.wtm.web.auth.util.JWTUtil;
//
//@Log4j2
//@RequiredArgsConstructor
//public class RefreshTokenFilterv1 extends OncePerRequestFilter {
//
//    private final String refreshPath;
//    private final JWTUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//        FilterChain filterChain) throws ServletException, IOException {
//
//        String path = request.getRequestURI();
//
//        // refreshPath와 일치하지 않으면 즉시 필터 통과
//        if (!path.equals(refreshPath)) {
//            log.info("skip refresh token filter......");
//            filterChain.doFilter(request, response);
//            return;
//        }
//        log.info("Executing RefreshTokenFilter for refresh token path...");
//
//        // 토큰 정보를 JSON에서 가져오기
//        Map<String, String> tokens = parseRequestJSON(request);
//        if (tokens == null) {
//            log.error("Token parsing failed. Invalid JSON format.");
//            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request format.");
//            return;
//        }
//
//        String accessToken = tokens.get("accessToken");
//        String refreshToken = tokens.get("refreshToken");
//        // Access Token check
//        try {
//          checkAccessToken(accessToken);
//        } catch(RefreshTokenException refreshTokenException) {
//            refreshTokenException.sendResponseError(response);
//            return; // 더이상 실행할 코드가 없음
//        }
//
//        Map<String, Object> refreshClaims = null;
//
//        // Refresh Token check
//        try {
//            refreshClaims = checkRefreshToken(refreshToken);
//            log.info(refreshClaims);
//        } catch(RefreshTokenException refreshTokenException) {
//            refreshTokenException.sendResponseError(response);
//            return; // 더이상 실행할 코드가 없음
//        }
//
//        // 새로운 Access Token 발행, 만료일이 머지 않은 Refresh Token 재발행
//        refreshClaims = checkRefreshToken(refreshToken);
//        log.info(refreshClaims);
//
//        // Refresh Token의 유효 시간이 얼마 남지 않은 경우
//        Long exp = (Long) refreshClaims.get("exp");
//
//        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
//        Date current = new Date(System.currentTimeMillis());
//
//        // 만료 시간과 현재 시간의 간격 계산
//        // 만일 3일 미만인 경우에는 Refresh Token도 다시 생성
//        long gapTime = (expTime.getTime() - current.getTime());
//
//        log.info("----------------------------------------");
//        log.info("current: " + current);
//        log.info("expTime: " + expTime);
//        log.info("gapTime: " + gapTime);
//
//        String username = (String) refreshClaims.get("username");
//
//        // 이 상태까지 오면 무조건 AccessToken은 새로 생성
//        String accessTokenValue = jwtUtil.generateToken(Map.of("username", username), JWTUtil.ACCESS_TOKEN_EXPIRATION_TIME);
//        String refreshTokenValue = tokens.get("refreshToken");
//
//        // RefreshToken이 3일도 안남았다면..
//        if(gapTime < (1000 * 60 * 60 * 24 * 3)) {
//            log.info("new Refresh Token required... ");
//            refreshTokenValue = jwtUtil.generateToken(Map.of("username", username), JWTUtil.REFRESH_TOKEN_EXPIRATION_TIME);
//        }
//
//        log.info("Refresh Token result.....................................");
//        log.info("accessToken: " + accessTokenValue);
//        log.info("refreshToken: " + refreshTokenValue);
//
//        sendTokens(accessTokenValue, refreshTokenValue, response);
//
//    }
//
//    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response) {
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(Map.of("accessToken", accessTokenValue,
//            "refreshToken", refreshTokenValue));
//
//        try {
//            response.getWriter().println(jsonStr);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private Map<String, String> parseRequestJSON(HttpServletRequest request) {
//
//        // JSON 데이터를 분석해서 username, password 전달 값을 Map으로 처리
//        try (Reader reader = new InputStreamReader(request.getInputStream())) {
//            Gson gson = new Gson();
//
//            return gson.fromJson(reader, Map.class);
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        return null;
//    }
//
//    private void checkAccessToken(String accessToken) throws RefreshTokenException {
//        try {
//            jwtUtil.validateToken(accessToken);
//        } catch (ExpiredJwtException expiredJwtException) {
//            log.info("Access Token has expired");
//        } catch (Exception exception) {
//            throw new RefreshTokenException(ErrorCase.NO_ACCESS);
//        }
//    }
//
//    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException {
//        try {
//            Map<String, Object> values = jwtUtil.validateToken(refreshToken);
//            return values;
//        } catch (ExpiredJwtException expiredJwtException) {
//            throw new RefreshTokenException(ErrorCase.OLD_REFRESH);
//        } catch (MalformedJwtException malformedJwtException) {
//            log.error("MalformedJwtException-----------------------");
//            throw new RefreshTokenException(ErrorCase.NO_REFRESH);
//        } catch (Exception exception) {
//            throw new RefreshTokenException(ErrorCase.NO_REFRESH);
//        }
//    }
//}
