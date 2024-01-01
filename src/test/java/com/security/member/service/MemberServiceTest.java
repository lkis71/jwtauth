package com.security.member.service;

import com.security.member.dto.MemberJoinDto;
import com.security.member.dto.MemberLoginDto;
import com.security.member.entity.Member;
import com.security.member.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    public void join() {

        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
                        .id("kslee")
                        .password("9156")
                        .role(Role.ADMIN.getValue())
                        .build();

        memberService.join(memberJoinDto);
    }

    @Test
    public void login() throws Exception {

//        //given
//        MemberJoinDto memberJoinDto = MemberJoinDto.builder()
//                .id("kslee")
//                .password("9156")
//                .auth("auth")
//                .build();
//
//        MemberLoginDto memberLoginDto = new MemberLoginDto();
//        memberLoginDto.setId("kslee");
//
//        //when
//        memberService.join(memberJoinDto);
//        Member login = memberService.login(memberLoginDto);
//        System.out.println("login = " + login);


        //then
    }
}