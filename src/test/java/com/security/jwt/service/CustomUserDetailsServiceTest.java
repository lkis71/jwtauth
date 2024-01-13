package com.security.jwt.service;

import com.security.member.dto.MemberJoinDto;
import com.security.member.enums.Role;
import com.security.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class CustomUserDetailsServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    MemberJoinDto memberJoinDto = new MemberJoinDto();

    @BeforeEach
    void init() {
        memberJoinDto = MemberJoinDto.builder()
                .id("kslee")
                .password("9156")
                .role(Role.ADMIN.getValue())
                .build();
    }

    @Test
    @DisplayName("사용자 ID로 조회한다.")
    void loadUserByUsername() {

        //given
        memberService.join(memberJoinDto);

        //when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(memberJoinDto.getId());

        //then
        assertEquals(memberJoinDto.getId(), userDetails.getUsername());
        assertTrue(passwordEncoder.matches(memberJoinDto.getPassword(), userDetails.getPassword()));
        assertTrue(userDetails.getAuthorities().toString().contains(memberJoinDto.getRole()));
    }
}