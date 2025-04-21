package com.security.redis.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisPrefix {

    REFRESH_TOKEN("refreshToken"),
    BLACKLIST("blacklist");

    private String value;
}
