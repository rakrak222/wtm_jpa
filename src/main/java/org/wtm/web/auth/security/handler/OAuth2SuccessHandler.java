package org.wtm.web.auth.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.wtm.web.auth.util.JWTUtil;

@Component
@RequiredArgsConstructor
@Log4j2
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        log.info("Login Success Handler--------------------");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        log.info(authentication);
        log.info(authentication.getName()); // username

        Map<String,Object> claim = Map.of("username", authentication.getName());

        // Access Token 유효 기간 1일
        String accessToken = jwtUtil.generateToken(claim, JWTUtil.ACCESS_TOKEN_EXPIRATION_TIME);
        // Refresh Token 유효 기간 30일
        String refreshToken = jwtUtil.generateToken(claim, JWTUtil.REFRESH_TOKEN_EXPIRATION_TIME);

        Gson gson = new Gson();

        Map<String, String> keyMap = Map.of("access_token", accessToken, "refresh_token", refreshToken);
        String jsonStr = gson.toJson(keyMap);

//        response.getWriter().write(jsonStr);
        response.sendRedirect("http://localhost:3000/auth/oauth-response/" + jsonStr + "/3600");
    }
}

