package com.security.redis;

import com.security.repository.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenStore implements RedisStore {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Override
    public void set(String memberId, String refreshToken, Long expiredTime) {
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(memberId)
                .refreshToken(refreshToken)
                .expiration(expiredTime)
                .build());
    }

    @Override
    public String get(String memberId) {
        return refreshTokenRedisRepository.findById(memberId).orElse(new RefreshToken()).getId();
    }

    @Override
    public void delete(String memberId) {
        refreshTokenRedisRepository.deleteById(memberId);
    }
}
