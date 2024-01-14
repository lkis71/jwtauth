package com.security.controller;

import com.security.dto.MemberJoinRequest;
import com.security.dto.MemberLoginRequest;
import com.security.dto.TokenResponse;
import com.security.entity.Member;
import com.security.dto.TokenStorageRequest;
import com.security.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody MemberJoinRequest memberJoinRequest) {
        Member member = memberService.join(memberJoinRequest);
        return ResponseEntity.ok().body("success join");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody MemberLoginRequest memberLoginRequest) {
        TokenResponse tokenResponse = memberService.login(memberLoginRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenStorageRequest tokenStorageRequest) {
        TokenResponse tokenResponse = memberService.refreshToken(tokenStorageRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }
}
