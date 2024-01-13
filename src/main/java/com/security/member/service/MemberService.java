package com.security.member.service;

import com.security.jwt.JwtTokenProvider;
import com.security.jwt.dto.TokenDto;
import com.security.jwt.dto.TokenStorageRequest;
import com.security.jwt.repository.JwtRepository;
import com.security.jwt.service.JwtService;
import com.security.member.dto.MemberJoinDto;
import com.security.member.dto.MemberLoginDto;
import com.security.member.entity.Member;
import com.security.member.entity.TokenStorage;
import com.security.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final JwtRepository jwtRepository;

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

        Member member = findMember(memberLoginDto.getId());
        // 평문 비밀번호와 암호화된 비밀번호 일치성 검사
        if (!passwordEncoder.matches(memberLoginDto.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Authentication authentication = jwtService.authentication(member.getId(), member.getPassword());

        // jwt 토큰 생성
        TokenDto tokenDto = jwtTokenProvider.createToken(authentication);

        // refreshToken 저장
        jwtRepository.save(TokenStorage.builder()
                .refreshToken(tokenDto.getRefreshToken())
                .member(findMember(memberLoginDto.getId()))
                .build());

        return tokenDto;
    }

    /**
     * 토큰 갱신 절차
     * 1. DB에 저장된 사용자의 토큰 정보 조회
     * 2. DB에서 조회한 사용자의 토큰 정보와 갱신 요청한 사용자의 정보가 일치한지 검사
     * 3. 토큰 재발급을 위한 사용자 정보 조회
     * 4. 사용자 인증
     * 5. 토큰 재발급
     * @param tokenStorageRequest
     * @return
     */
    @Transactional
    public TokenDto refreshToken(TokenStorageRequest tokenStorageRequest) {

        TokenStorage tokenStorage = findMemberToken(tokenStorageRequest.getRefreshToken());

        if (tokenStorage.isValidToken(tokenStorageRequest)) {
            Member member = findMember(tokenStorage.getMember().getId());

            Authentication authentication = jwtService.authentication(member.getId(), member.getPassword());

            TokenDto refreshTokenDto = jwtTokenProvider.createToken(authentication);
            tokenStorage.setRefreshToken(refreshTokenDto.getRefreshToken());

            return refreshTokenDto;
        } else {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
    }

    private TokenStorage findMemberToken(String refreshToken) {
        TokenStorage tokenStorage = jwtRepository.findMemberToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("토큰이 유효하지 않습니다."));
        return tokenStorage;
    }

    private Member findMember(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        return member;
    }

}
