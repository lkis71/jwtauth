package com.security.member.service;

import com.security.dto.TokenResponse;
import com.security.service.CustomUserDetailsService;
import com.security.dto.MemberJoinRequest;
import com.security.dto.MemberLoginRequest;
import com.security.entity.Member;
import com.security.enums.Role;
import com.security.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("회원가입")
    public void join() {

        //given
        MemberJoinRequest memberJoinRequest = MemberJoinRequest.builder()
                        .id("kslee")
                        .password("9156")
                        .role(Role.ADMIN.getValue())
                        .build();

        //when
        Member member = memberService.join(memberJoinRequest);

        //then
        assertEquals(memberJoinRequest.getId(), member.getId());
        assertEquals(memberJoinRequest.getPassword(), member.getPassword());
        assertEquals(memberJoinRequest.getRole(), member.getRole());
    }

    @Test
    @DisplayName("로그인")
    public void login() throws Exception {

        //given
        MemberJoinRequest memberJoinRequest = MemberJoinRequest.builder()
                .id("kslee")
                .password("9156")
                .role(Role.ADMIN.getValue())
                .build();
        Member member = memberService.join(memberJoinRequest);

        //when
        MemberLoginRequest memberLoginRequest = MemberLoginRequest.builder()
                .id("kslee")
                .password("9156")
                .build();
        TokenResponse tokenResponse = memberService.login(memberLoginRequest);

        System.out.println(tokenResponse.getRefreshToken());

        //then
        assertNotNull(tokenResponse.getAccessToken());
        assertNotNull(tokenResponse.getRefreshToken());
    }
}