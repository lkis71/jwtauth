package com.security.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@Builder
@RedisHash("refreshToken")
public class RefreshToken {

    @Id
    private String id;

    @Setter
    @Indexed
    private String refreshToken;
}
