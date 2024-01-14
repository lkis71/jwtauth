package com.security.service;

import com.security.dto.MemberJoinRequest;
import com.security.dto.MemberLoginRequest;
import com.security.dto.TokenResponse;
import com.security.dto.TokenStorageRequest;
import com.security.entity.Member;
import com.security.entity.TokenStorage;
import com.security.util.JwtTokenProvider;
import com.security.repository.JwtRepository;
import com.security.repository.MemberRepository;
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
    public Member join(MemberJoinRequest memberJoinRequest) {

        Member member = memberJoinRequest.toEntity();
        member.setPassword(passwordEncoder.encode(memberJoinRequest.getPassword()));
        memberRepository.save(member);

        return member;
    }

    @Transactional
    public TokenResponse login(MemberLoginRequest memberLoginRequest) {

        Member member = findMember(memberLoginRequest.getId());
        // 평문 비밀번호와 암호화된 비밀번호 일치성 검사
        if (!passwordEncoder.matches(memberLoginRequest.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Authentication authentication = jwtService.authentication(member.getId(), member.getPassword());

        // jwt 토큰 생성
        TokenResponse tokenResponse = jwtTokenProvider.createToken(authentication);

        // refreshToken 저장
        jwtRepository.save(TokenStorage.builder()
                .refreshToken(tokenResponse.getRefreshToken())
                .member(findMember(memberLoginRequest.getId()))
                .build());

        return tokenResponse;
    }

    public void logout() {
        //todo
        //갱신토큰 파괴
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
    public TokenResponse refreshToken(TokenStorageRequest tokenStorageRequest) {

        TokenStorage tokenStorage = findMemberToken(tokenStorageRequest.getRefreshToken());

        if (!tokenStorage.isValidToken(tokenStorageRequest)) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }

        Member member = findMember(tokenStorage.getMember().getId());
        Authentication authentication = jwtService.authentication(member.getId(), member.getPassword());

        TokenResponse refreshTokenResponse = jwtTokenProvider.createToken(authentication);
        tokenStorage.setRefreshToken(refreshTokenResponse.getRefreshToken());

        return refreshTokenResponse;
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
