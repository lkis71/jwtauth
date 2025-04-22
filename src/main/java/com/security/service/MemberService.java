package com.security.service;

import com.security.dto.JoinRequest;
import com.security.dto.loginRequest;
import com.security.dto.TokenRefreshRequest;
import com.security.dto.TokenResponse;
import com.security.entity.Member;
import com.security.redis.RedisTemplateStore;
import com.security.redis.enums.RedisPrefix;
import com.security.repository.MemberRepository;
import com.security.util.JwtTokenProvider;
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

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplateStore redisStore;

    @Transactional
    public Member join(JoinRequest joinRequest) {

        Member member = joinRequest.toEntity();
        member.setPassword(passwordEncoder.encode(joinRequest.getPassword()));
        memberRepository.save(member);

        return member;
    }

    @Transactional
    public TokenResponse login(loginRequest loginRequest) {

        Member member = findMember(loginRequest.getId());
        // 평문 비밀번호와 암호화된 비밀번호 일치성 검사
        if (!passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 사용자 인증
        Authentication authentication = jwtService.authentication(member.getId(), member.getPassword());

        // 토큰 발급 및 리프레시 토큰 저장
        TokenResponse tokenResponse = jwtTokenProvider.createToken(authentication);
        saveRefreshTokenInRedis(tokenResponse, member);

        return tokenResponse;
    }

    @Transactional
    public void logout(String memberId, String accessToken) {

        redisStore.delete(RedisPrefix.REFRESH_TOKEN.getValue() + ":" + memberId);

        // accessToken 블랙리스트 등록
        redisStore.set(RedisPrefix.BLACKLIST.getValue() + ":" + memberId,
                accessToken, jwtTokenProvider.getExpirationMillis(accessToken));
    }

    @Transactional
    public TokenResponse refreshToken(TokenRefreshRequest tokenRefreshRequest) {

        String memberId = tokenRefreshRequest.getMemberId();

        // 토큰 검증
        String originToken = redisStore.get(RedisPrefix.REFRESH_TOKEN.getValue() + ":" + memberId);
        if (!originToken.equals(tokenRefreshRequest.getRefreshToken())) {
            throw new IllegalArgumentException();
        }

        redisStore.delete(RedisPrefix.REFRESH_TOKEN.getValue() + ":" + memberId);

        // 사용자 인증
        Member member = findMember(memberId);
        Authentication authentication = jwtService.authentication(memberId, member.getPassword());

        // 토큰 재발급 및 리프레시 토큰 저장
        TokenResponse tokenResponse = jwtTokenProvider.createToken(authentication);
        saveRefreshTokenInRedis(tokenResponse, member);

        return tokenResponse;
    }

    private void saveRefreshTokenInRedis(TokenResponse tokenResponse, Member member) {
        String refreshToken = tokenResponse.getRefreshToken();
        redisStore.set(RedisPrefix.REFRESH_TOKEN.getValue() + ":" + member.getId(),
                refreshToken, jwtTokenProvider.getExpirationMillis(refreshToken));
    }

    private Member findMember(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        return member;
    }
}