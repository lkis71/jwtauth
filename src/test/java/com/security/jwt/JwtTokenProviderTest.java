package com.security.jwt;

import com.security.jwt.dto.TokenDto;
import com.security.member.dto.MemberJoinDto;
import com.security.member.dto.MemberLoginDto;
import com.security.member.enums.Role;
import com.security.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
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

    MemberJoinDto memberJoinDto = new MemberJoinDto();
    MemberLoginDto memberLoginDto = new MemberLoginDto();

    @BeforeEach
    void init() {
        memberJoinDto = MemberJoinDto.builder()
                .id("kslee")
                .password("9156")
                .role(Role.ADMIN.getValue())
                .build();

        memberLoginDto.setId("kslee");
        memberLoginDto.setPassword("9156");
    }

    @Test
    void generateToken() {

        //given
        memberService.join(this.memberJoinDto);

        //when
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(this.memberLoginDto.getId(), this.memberLoginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        //then
        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    void getAuthentication() {

        //given
        memberService.join(this.memberJoinDto);

        //when
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(this.memberLoginDto.getId(), this.memberLoginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        Authentication authentication2 = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());

        //then
        assertTrue(authentication2.isAuthenticated());
    }

    @Test
    void validateToken() {

        //given
        memberService.join(this.memberJoinDto);

        //when
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(this.memberLoginDto.getId(), this.memberLoginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        boolean isToken = jwtTokenProvider.validateToken(tokenDto.getAccessToken());

        //then
        assertTrue(isToken);
    }
}