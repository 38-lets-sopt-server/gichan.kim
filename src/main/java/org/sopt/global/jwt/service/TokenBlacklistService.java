package org.sopt.global.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private static final String BLACKLIST_PREFIX = "BL:";
    private static final String LOGOUT_VALUE = "logout";

    private final RedisTemplate<String, String> redisTemplate;

    public void addToBlacklist(String accessToken, long remainingMillis) {
        if (remainingMillis <= 0) return;
        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + accessToken,
                LOGOUT_VALUE,
                remainingMillis,
                TimeUnit.MILLISECONDS
        );
    }

    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + accessToken);
    }
}
