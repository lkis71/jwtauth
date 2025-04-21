package com.security.redis;

public interface RedisStore {

    void set(String key, String value, Long expiredTime);
    String get(String key);
    void delete(String key);
}
