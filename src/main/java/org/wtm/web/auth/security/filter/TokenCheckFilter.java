package org.wtm.web.auth.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.wtm.web.auth.security.APIUserDetailsService;
import org.wtm.web.auth.security.exception.AccessTokenException;
import org.wtm.web.auth.security.exception.AccessTokenException.TOKEN_ERROR;
import org.wtm.web.auth.util.JWTUtil;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

  private final APIUserDetailsService apiUserDetailsService;
  private final JWTUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String path = request.getRequestURI();

    if(!path.startsWith("/api/")){
      filterChain.doFilter(request, response);
      return;
    }

    log.info("Token Check Filter ..............................");
    log.info("JWTUtil: {}", jwtUtil);

    try {
      Map<String, Object> payload = validateAccessToken(request);

      // username
      String username = (String) payload.get("username");
      log.info("username : {}", username);

      UserDetails userDetails = apiUserDetailsService.loadUserByUsername(username);

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // 권한이 여러개일 수도 있으니

      SecurityContextHolder.getContext().setAuthentication(authentication);

      filterChain.doFilter(request, response);

    } catch (AccessTokenException accessTokenException){
      accessTokenException.sendResponseError(response);
    }
  }

  private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException {
    String headerStr = request.getHeader("Authorization");

    if (headerStr == null || headerStr.length() < 8) {
      throw new AccessTokenException(TOKEN_ERROR.UNACCEPTED);
    }

    // Bearer 생략
    String tokenType = headerStr.substring(0, 6);
    String tokenStr = headerStr.substring(7);

    if (!tokenType.equalsIgnoreCase("Bearer")) { throw new AccessTokenException(TOKEN_ERROR.BAD_TYPE); }

    try {
      return jwtUtil.validateToken(tokenStr);
    } catch (MalformedJwtException malformedJwtException) {
      log.error("MalformedJwtException----------------------------");
      throw new AccessTokenException(TOKEN_ERROR.MALFORMED);
    } catch (SignatureException signatureException) {
      log.error("SignatureException----------------------------");
      throw new AccessTokenException(TOKEN_ERROR.BAD_SIGNATURE);
    } catch (ExpiredJwtException expiredJwtException) {
      log.error("ExpiredJwtException----------------------------");
      throw new AccessTokenException(TOKEN_ERROR.EXPIRED_TOKEN);
    }
  }
}