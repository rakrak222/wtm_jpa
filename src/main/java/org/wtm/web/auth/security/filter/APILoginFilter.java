package org.wtm.web.auth.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {

    public APILoginFilter(String defaultFilterProcessesUrl){
        super(defaultFilterProcessesUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        log.info("APILoginFilter-----------------------------------");

        if(request.getMethod().equalsIgnoreCase("GET")){
            log.info("GET METHOD NOT SUPPORT");
            return null;
        }

        Map<String, String> jsonData = parseRequestJSON(request);

        if (jsonData == null || !jsonData.containsKey("email") || !jsonData.containsKey("password")) {
            log.error("Invalid JSON data or missing fields: {}", jsonData);
            throw new AuthenticationException("Invalid login request data") {};
        }

        log.info("Parsed JSON data: {}", jsonData);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            jsonData.get("email"),
            jsonData.get("password")
        );
        log.info("Created authentication token: {}", authenticationToken);

        try {
            Authentication authResult = getAuthenticationManager().authenticate(authenticationToken);
            log.info("Authentication successful: {}", authResult.isAuthenticated());
            return authResult;
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw e;
        }
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request) {

        try (Reader reader = new InputStreamReader(request.getInputStream())) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        } catch (IOException e) {
            log.error("Error parsing JSON request data: {}", e.getMessage());
            throw new AuthenticationException("Failed to parse JSON request data") {};
        }
    }
}