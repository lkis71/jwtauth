package com.security.jwt.service;

import com.security.jwt.dto.TokenDto;
import com.security.jwt.dto.TokenStorageRequest;
import com.security.member.dto.MemberJoinDto;
import com.security.member.dto.MemberLoginDto;
import com.security.member.entity.Member;
import com.security.member.enums.Role;
import com.security.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@Transactional
class JwtServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    public void 토큰갱신() throws Exception {

        //given
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .id("kslee")
                .password("9156")
                .role(Role.ADMIN.getValue())
                .build();
        Member member = memberService.join(memberJoinDto);

        MemberLoginDto memberLoginDto = MemberLoginDto.builder()
                .id("kslee")
                .password(member.getPassword())
                .build();
        TokenDto tokenDto = memberService.login(memberLoginDto);

        //when
        TokenStorageRequest tokenStorageRequest = TokenStorageRequest.builder()
                .refreshToken(tokenDto.getRefreshToken())
                .build();

        TokenDto refreshTokenDto = memberService.refreshToken(tokenStorageRequest);

        //then
        assertNotEquals(tokenDto.getAccessToken(), refreshTokenDto.getAccessToken());
        assertNotEquals(tokenDto.getRefreshToken(), refreshTokenDto.getRefreshToken());
    }
}