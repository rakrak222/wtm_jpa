package org.wtm.web.auth.util;

import static io.jsonwebtoken.Jwts.parser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JWTUtil {

  public static final int ACCESS_TOKEN_EXPIRATION_TIME = 1;
  public static final int REFRESH_TOKEN_EXPIRATION_TIME = 30;

  private static final MacAlgorithm SIGNATURE_ALGORITHM = SIG.HS256;
  private SecretKey key;

  @Value("${app.jwtSecret}")
  private String secretKey;

  // SecretKey 초기화
  private SecretKey getKey() {
    if (key == null) {
      byte[] secretKeyBytes = Base64.getEncoder().encode(secretKey.getBytes(StandardCharsets.UTF_8));
      key = Keys.hmacShaKeyFor(secretKeyBytes);
    }
    return key;
  }

  public String generateToken(Map<String, Object> claims, int days){

    int time = (1) * days;

    return Jwts.builder()
        .claims(claims)
        .issuedAt(Date.from(ZonedDateTime.now().toInstant()))
        .expiration(Date.from(ZonedDateTime.now().plusDays(time).toInstant()))
        .signWith(getKey(), SIGNATURE_ALGORITHM)
        .compact();

  }

  public Map<String, Object> validateToken(String token) {
    byte[] secretKeyBytes = Base64.getEncoder().encode(secretKey.getBytes());
    SecretKey key = Keys.hmacShaKeyFor(secretKeyBytes);

    JwtParser parser = parser()
        .verifyWith(key)
        .build();

    Claims claims = parser.parseSignedClaims(token).getPayload();
    claims.forEach((k, v) -> log.info("Claim - {} : {}", k, v));
    return claims;
  }
}
