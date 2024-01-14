package com.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public Authentication authentication(String id, String password) {

        // 실제 로그인한 아이디와 비밀번호를 인증 토큰에 저장한다.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(id, password);

        // 인증 토큰을 사용하여 실제 인증을 수행한다.
        // 추가로 이 과정에서 재정의한 CustomUserDetailsService의 loadUserByUsername()를 호출하여 DB에서 사용자 정보를 가져온다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
        // SecurityContextHolder에 사용자 인증 정보를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
}
