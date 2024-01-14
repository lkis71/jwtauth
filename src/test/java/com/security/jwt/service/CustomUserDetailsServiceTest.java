package com.security.jwt.service;

import com.security.dto.MemberJoinRequest;
import com.security.enums.Role;
import com.security.service.CustomUserDetailsService;
import com.security.service.MemberService;
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

    MemberJoinRequest memberJoinRequest = null;

    @BeforeEach
    void init() {
        memberJoinRequest = MemberJoinRequest.builder()
                .id("kslee")
                .password("9156")
                .role(Role.ADMIN.getValue())
                .build();
    }

    @Test
    @DisplayName("사용자 ID로 조회한다.")
    void loadUserByUsername() {

        //given
        memberService.join(memberJoinRequest);

        //when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(memberJoinRequest.getId());

        //then
        assertEquals(memberJoinRequest.getId(), userDetails.getUsername());
        assertTrue(passwordEncoder.matches(memberJoinRequest.getPassword(), userDetails.getPassword()));
        assertTrue(userDetails.getAuthorities().toString().contains(memberJoinRequest.getRole()));
    }
}