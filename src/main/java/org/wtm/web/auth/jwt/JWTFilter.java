package org.wtm.web.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.wtm.web.auth.dto.CustomOAuth2User;
import org.wtm.web.auth.dto.UserDto;
import org.wtm.web.auth.utils.JWTUtil;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (token == null || jwtUtil.isExpired(token)) {
            System.out.println("Token is either null or expired");
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰에서 username과 role 추출
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // userDto 생성 및 설정
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setRole(role);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);

        // 인증 토큰 생성 및 설정
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // 1. 쿠키에서 토큰 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authorization".equals(cookie.getName())) {
                    System.out.println("Token found in cookies");
                    return cookie.getValue();
                }
            }
        }

        // 2. 헤더에서 Bearer 토큰 가져오기
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            System.out.println("Token found in Authorization header");
            return authorizationHeader.substring(7); // "Bearer " 부분 제거
        }

        return null; // 토큰이 없으면 null 반환
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
//        String authorization = null;
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//
//            System.out.println(cookie.getName());
//            if (cookie.getName().equals("Authorization")) {
//
//                authorization = cookie.getValue();
//            }
//        }
//
//        //Authorization 헤더 검증
//        if (authorization == null) {
//
//            System.out.println("token null");
//            filterChain.doFilter(request, response);
//
//            //조건이 해당되면 메소드 종료 (필수)
//            return;
//        }
//
//        //토큰
//        String token = authorization;
//
//        //토큰 소멸 시간 검증
//        if (jwtUtil.isExpired(token)) {
//
//            System.out.println("token expired");
//            filterChain.doFilter(request, response);
//
//            //조건이 해당되면 메소드 종료 (필수)
//            return;
//        }
//
//        //토큰에서 username과 role 획득
//        String username = jwtUtil.getUsername(token);
//        String role = jwtUtil.getRole(token);
//
//        //userDTO를 생성하여 값 set
//        UserDto userDto = new UserDto();
//        userDto.setUsername(username);
//        userDto.setRole(role);
//
//        //UserDetails에 회원 정보 객체 담기
//        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDto);
//
//        //스프링 시큐리티 인증 토큰 생성
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
//        //세션에 사용자 등록
//        SecurityContextHolder.getContext().setAuthentication(authToken);
//
//        filterChain.doFilter(request, response);
//    }
}