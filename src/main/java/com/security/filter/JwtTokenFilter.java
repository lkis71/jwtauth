package com.security.filter;

import com.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
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

        String token = getToken(request);

        if (token != null) {
            if (jwtTokenProvider.isExpired(token)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("text/plain; charset=UTF-8");
                response.getWriter().write("토큰이 만료되었습니다. 토큰을 갱신해 주세요.");
                return;
            }

            // 토큰 유효성 검사
            if (!jwtTokenProvider.validateToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            // JWT 토큰을 검증하고 토큰에 저장된 정보를 UsernamePasswordAuthenticationToken에 담아 SecurityContextHolder에 저장한다.
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    // Header에서 토큰 꺼내기
    private String getToken(HttpServletRequest request) {
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer")) {
            return null;
        }
       return authorization.substring(7);
    }
}
