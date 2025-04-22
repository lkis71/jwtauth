package com.security.jwt;

import com.security.dto.JoinRequest;
import com.security.dto.loginRequest;
import com.security.dto.TokenResponse;
import com.security.service.MemberService;
import com.security.util.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    MemberService memberService;

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    JoinRequest joinRequest = null;
    loginRequest loginRequest = null;

    @BeforeEach
    void init() {
        joinRequest = JoinRequest.builder()
                .id("kslee")
                .password("9156")
                .role("ADMIN")
                .build();

//        memberLoginRequest.setId("kslee");
//        memberLoginRequest.setPassword("9156");
    }

    @Test
    @DisplayName("JWT 토큰을 발행한다.")
    void createToken() {

        //given
        memberService.join(this.joinRequest);

        //when
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(this.loginRequest.getId(), this.loginRequest.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenResponse tokenResponse = jwtTokenProvider.createToken(authentication);

        //then
        assertNotNull(tokenResponse.getAccessToken());
        assertNotNull(tokenResponse.getRefreshToken());
    }

    @Test
    @DisplayName("인증정보를 조회한다.")
    void getAuthentication() {

        //given
        memberService.join(this.joinRequest);

        //when
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(this.loginRequest.getId(), this.loginRequest.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenResponse tokenResponse = jwtTokenProvider.createToken(authentication);

        Authentication authentication2 = jwtTokenProvider.getAuthentication(tokenResponse.getAccessToken());

        //then
        assertTrue(authentication2.isAuthenticated());
    }

    @Test
    @DisplayName("토큰 유효성을 검증한다.")
    void validateToken() {

        //given
        memberService.join(this.joinRequest);

        //when
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(this.loginRequest.getId(), this.loginRequest.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenResponse tokenResponse = jwtTokenProvider.createToken(authentication);

        boolean isToken = jwtTokenProvider.validateToken(tokenResponse.getAccessToken());

        //then
        assertTrue(isToken);
    }
}