package com.security.controller;

import com.security.dto.MemberJoinRequest;
import com.security.dto.MemberLoginRequest;
import com.security.dto.TokenResponse;
import com.security.entity.Member;
import com.security.service.MemberService;
import com.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/join")
    public ResponseEntity<Member> join(@RequestBody MemberJoinRequest memberJoinRequest) {

        Member member = memberService.join(memberJoinRequest);
        return ResponseEntity.ok().body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody MemberLoginRequest memberLoginRequest) {

        TokenResponse tokenResponse = memberService.login(memberLoginRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {

        String token = jwtTokenProvider.getToken(request);
        TokenResponse tokenResponse = memberService.refreshToken(token);
        return ResponseEntity.ok().body(tokenResponse);
    }
}