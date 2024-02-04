package com.security.repository;

import com.security.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

    RefreshToken findByRefreshToken(String refreshToken);
}
