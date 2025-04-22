package com.security.filter;

import com.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("X-User-Token");

        if (token != null) {
            // JWT 토큰을 검증하고 토큰에 저장된 정보를 UsernamePasswordAuthenticationToken에 담아 SecurityContextHolder에 저장한다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        String token = jwtTokenProvider.extractToken(request);
//
//        // Refresh Token 의 경우 토큰 검증을 하지 않음
//        if (isRequestRefreshToken(request)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        if (StringUtils.hasText(token)) {
//            // 토큰 유효성 검사
//            if (!jwtTokenProvider.validateToken(token)) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Invalid JWT Token");
//                return;
//            }
//
//            // JWT 토큰을 검증하고 토큰에 저장된 정보를 UsernamePasswordAuthenticationToken에 담아 SecurityContextHolder에 저장한다.
//            Authentication authentication = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private boolean isRequestRefreshToken(HttpServletRequest request) {
//        return request.getRequestURI().contains("/token/refresh");
//    }
}
