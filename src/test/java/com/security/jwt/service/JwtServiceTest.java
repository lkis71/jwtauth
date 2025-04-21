package com.security.jwt.service;

import com.security.dto.TokenResponse;
import com.security.dto.MemberJoinRequest;
import com.security.dto.MemberLoginRequest;
import com.security.entity.Member;
import com.security.enums.Role;
import com.security.service.MemberService;
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

//    @Test
//    public void 토큰갱신() throws Exception {
//
//        //given
//        MemberJoinRequest memberJoinRequest = MemberJoinRequest.builder()
//                .id("kslee")
//                .password("9156")
//                .role(Role.ADMIN.getValue())
//                .build();
//        Member member = memberService.join(memberJoinRequest);
//
//        MemberLoginRequest memberLoginRequest = MemberLoginRequest.builder()
//                .id("kslee")
//                .password(member.getPassword())
//                .build();
//        TokenResponse tokenResponse = memberService.login(memberLoginRequest);
//
//        //when
//        TokenResponse refreshTokenResponse = memberService.refreshToken(tokenResponse.getRefreshToken());
//
//        //then
//        assertNotEquals(tokenResponse.getAccessToken(), refreshTokenResponse.getAccessToken());
//        assertNotEquals(tokenResponse.getRefreshToken(), refreshTokenResponse.getRefreshToken());
//    }
}