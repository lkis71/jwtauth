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
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public Member join(MemberJoinDto memberJoinDto) {

        Member member = memberJoinDto.toEntity();
        memberRepository.save(member);

        return member;
    }

    @Transactional
    public TokenDto login(MemberLoginDto memberLoginDto) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginDto.getId(), memberLoginDto.getPassword());

        // 사용자 인증
        // 이 과정에서 CustomUserDetailsService 에서 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = jwtTokenProvider.createToken(authentication);

        // refreshToken 저장
        Member member = memberRepository.findById(memberLoginDto.getId()).orElseThrow(IllegalArgumentException::new);
        member.setRefreshToken(tokenDto.getRefreshToken());

        // jwt 토큰 생성
        return tokenDto;
    }
}
