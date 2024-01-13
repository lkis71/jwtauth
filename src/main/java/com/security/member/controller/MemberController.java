package com.security.member.controller;

import com.security.jwt.dto.TokenDto;
import com.security.jwt.dto.TokenStorageRequest;
import com.security.member.dto.MemberJoinDto;
import com.security.member.dto.MemberLoginDto;
import com.security.member.entity.Member;
import com.security.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Member> join(@RequestBody MemberJoinDto memberJoinDto) {
        Member member = memberService.join(memberJoinDto);
        return ResponseEntity.ok().body(member);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody MemberLoginDto memberLoginDto, @RequestHeader("User-Agent") String userAgent) {
        TokenDto tokenDto = memberService.login(memberLoginDto);
        return ResponseEntity.ok().body(tokenDto);
    }

    @GetMapping("/token/refresh")
    public ResponseEntity<TokenDto> refreshToken(@RequestBody TokenStorageRequest tokenStorageRequest) {
        TokenDto tokenDto = memberService.refreshToken(tokenStorageRequest);
        return ResponseEntity.ok().body(tokenDto);
    }
}
