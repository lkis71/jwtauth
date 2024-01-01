package com.security.member.service;

import com.security.jwt.JwtTokenProvider;
import com.security.jwt.dto.TokenDto;
import com.security.member.dto.MemberJoinDto;
import com.security.member.dto.MemberLoginDto;
import com.security.member.entity.Member;
import com.security.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public Member join(MemberJoinDto memberJoinDto) {

        Member member = memberJoinDto.toEntity();
        memberRepository.save(member);

        return member;
    }

    public TokenDto login(MemberLoginDto memberLoginDto) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginDto.getId(), memberLoginDto.getPassword());

        // 실제 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // jwt 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);

        return tokenDto;
    }
}
