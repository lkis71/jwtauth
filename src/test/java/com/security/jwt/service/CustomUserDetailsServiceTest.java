package com.security.jwt.service;

import com.security.dto.JoinRequest;
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

    JoinRequest joinRequest = null;

    @BeforeEach
    void init() {
        joinRequest = JoinRequest.builder()
                .id("kslee")
                .password("9156")
                .role("ADMIN")
                .build();
    }

    @Test
    @DisplayName("사용자 ID로 조회한다.")
    void loadUserByUsername() {

        //given
        memberService.join(joinRequest);

        //when
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(joinRequest.getId());

        //then
        assertEquals(joinRequest.getId(), userDetails.getUsername());
        assertTrue(passwordEncoder.matches(joinRequest.getPassword(), userDetails.getPassword()));
        assertTrue(userDetails.getAuthorities().toString().contains(joinRequest.getRole()));
    }
}