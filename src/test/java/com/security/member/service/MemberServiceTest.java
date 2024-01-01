package com.security.member.service;

import com.security.jwt.dto.TokenDto;
import com.security.member.dto.MemberJoinDto;
import com.security.member.dto.MemberLoginDto;
import com.security.member.entity.Member;
import com.security.member.enums.Role;
import org.junit.jupiter.api.Assertions;
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

    @Test
    @DisplayName("회원가입")
    public void join() {

        //given
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                        .id("kslee")
                        .password("9156")
                        .role(Role.ADMIN.getValue())
                        .build();

        //when
        Member member = memberService.join(memberJoinDto);

        //then
        assertEquals(memberJoinDto.getId(), member.getId());
        assertEquals(memberJoinDto.getPassword(), member.getPassword());
        assertEquals(memberJoinDto.getRole(), member.getRole());
    }

    @Test
    @DisplayName("로그인")
    public void login() throws Exception {

        //given
        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                .id("kslee")
                .password("9156")
                .role(Role.ADMIN.getValue())
                .build();

        MemberLoginDto memberLoginDto = new MemberLoginDto();
        memberLoginDto.setId("kslee");
        memberLoginDto.setPassword("9156");

        //when
        memberService.join(memberJoinDto);
        TokenDto tokenDto = memberService.login(memberLoginDto);

        //then
        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }
}