package com.security.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@Builder
@RedisHash("refreshToken")
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String id;

    @Setter
    @Indexed
    private String refreshToken;

    @TimeToLive
    private Long expiration;
}
