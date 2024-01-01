package com.security.member.controller;

import com.security.member.dto.MemberJoinDto;
import com.security.member.dto.MemberLoginDto;
import com.security.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/v1/member")
    public void join(@RequestBody MemberJoinDto memberJoinDto) {

        memberService.join(memberJoinDto);
    }

    @PostMapping("/api/v1/login")
    public void login(@RequestBody MemberLoginDto memberLoginDto) {

        memberService.login(memberLoginDto);
    }
}
