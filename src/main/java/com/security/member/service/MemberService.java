package com.security.member.service;

import com.security.jwt.JwtTokenProvider;
import com.security.jwt.dto.TokenDto;
import com.security.member.dto.MemberJoinDto;
import com.security.member.dto.MemberLoginDto;
import com.security.member.entity.Member;
import com.security.member.entity.TokenStorage;
import com.security.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member join(MemberJoinDto memberJoinDto) {

        Member member = memberJoinDto.toEntity();
        member.setPassword(passwordEncoder.encode(memberJoinDto.getPassword()));
        memberRepository.save(member);

        return member;
    }

    @Transactional
    public TokenDto login(MemberLoginDto memberLoginDto) {

        // 실제 로그인한 아이디와 비밀번호를 인증 토큰에 저장한다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberLoginDto.getId(), memberLoginDto.getPassword());

        // 인증 토큰을 사용하여 실제 인증을 수행한다.
        // 추가로 이 과정에서 재정의한 CustomUserDetailsService의 loadUserByUsername()를 호출하여 DB에서 사용자 정보를 가져온다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // SecurityContextHolder에 사용자 인증 정보를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // jwt 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.createToken(authentication);

        // refreshToken 저장
        TokenStorage tokenStorage = TokenStorage.builder()
                .id(memberLoginDto.getId())
                .refreshToken(tokenDto.getRefreshToken())
                .build();

        return tokenDto;
    }
}
