package com.security.service;

import com.security.dto.MemberJoinRequest;
import com.security.dto.MemberLoginRequest;
import com.security.dto.TokenResponse;
import com.security.entity.Member;
import com.security.redis.enums.RedisPrefix;
import com.security.redis.RedisTemplateStore;
import com.security.repository.MemberRepository;
import com.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
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

        // 토큰 발급
        TokenResponse tokenResponse = jwtTokenProvider.createToken(authentication);

        String refreshToken = tokenResponse.getRefreshToken();
        redisStore.set(RedisPrefix.REFRESH_TOKEN.getValue() + ":" + member.getId(),
                refreshToken, jwtTokenProvider.getExpirationMillis(refreshToken));

        return tokenResponse;
    }

    public void logout(User user, String accessToken) {

        redisStore.delete(RedisPrefix.REFRESH_TOKEN.getValue() + ":" + user.getUsername());

//        redisStore.set(RedisPrefix.BLACKLIST.getValue() + ":" + user.getUsername(),
//                accessToken, jwtTokenProvider.getExpirationMillis(accessToken));
    }

    @Transactional
    public TokenResponse refreshToken(String memberId, String token) {

        // 토큰 검증
        String originToken = redisStore.get(RedisPrefix.REFRESH_TOKEN.getValue() + ":" + memberId);
        if (!originToken.equals(token)) {
            throw new IllegalArgumentException();
        }

        redisStore.delete(RedisPrefix.REFRESH_TOKEN.getValue() + ":" + memberId);

        // 사용자 인증
        Member member = findMember(memberId);
        Authentication authentication = jwtService.authentication(memberId, member.getPassword());

        // 토큰 재발급
        TokenResponse tokenResponse = jwtTokenProvider.createToken(authentication);
        // 리프레시 토큰 저장
        String refreshToken = tokenResponse.getRefreshToken();
        redisStore.set(RedisPrefix.REFRESH_TOKEN.getValue() + ":" + member.getId(),
                refreshToken, jwtTokenProvider.getExpirationMillis(refreshToken));

        return tokenResponse;
    }

    private Member findMember(String memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        return member;
    }
}