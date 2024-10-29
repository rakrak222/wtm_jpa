package org.wtm.web.user.auth.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.wtm.web.user.auth.service.CustomUserDetailsService;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final CustomUserDetailsService customUserDetailsService;

    private SecretKey secretKey;

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private long jwtExpirationMs;


    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String createToken(Authentication authentication) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername(); // 이메일로 사용

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(email) // 이메일을 subject에 저장
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        // 1. 토큰에서 이메일 추출
        String email = getEmailFromToken(token);
        
        // 2. 이메일 통해 사용자 정보 로드
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        // 3. UserDetails를 기반으로 Authentication 객체 생성
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }

    private String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // 토큰 검증을 위해 설정된 비밀 키
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // 'subject'에 사용자명을 저장했다면 추출
    }
}
