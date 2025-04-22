package com.security.redis;

import com.security.repository.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenStore implements RedisStore {

    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Override
    public void set(String key, String value, Long expiredTime) {
        refreshTokenRedisRepository.save(RefreshToken.builder()
                .id(key)
                .refreshToken(value)
                .expiration(expiredTime)
                .build());
    }

    @Override
    public String get(String key) {
        return refreshTokenRedisRepository.findById(key).orElse(new RefreshToken()).getId();
    }

    @Override
    public void delete(String key) {
        refreshTokenRedisRepository.deleteById(key);
    }
}
