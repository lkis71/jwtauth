package com.security.controller;

import com.security.dto.JoinRequest;
import com.security.dto.TokenRefreshRequest;
import com.security.dto.TokenResponse;
import com.security.dto.loginRequest;
import com.security.entity.Member;
import com.security.service.MemberService;
import com.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/join")
    public ResponseEntity<Member> join(@RequestBody JoinRequest joinRequest) {

        Member member = memberService.join(joinRequest);
        return ResponseEntity.ok().body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody loginRequest loginRequest) {

        TokenResponse tokenResponse = memberService.login(loginRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, @AuthenticationPrincipal User user) {

        String token = request.getHeader("X-User-Token");
//        String token = jwtTokenProvider.extractToken(request);
        memberService.logout(user.getUsername(), token);
        return ResponseEntity.ok().body("success");
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {

        TokenResponse tokenResponse = memberService.refreshToken(tokenRefreshRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(HttpServletRequest request) {
        return ResponseEntity.ok().body(request.getHeader("X-User-Token"));
    }
}