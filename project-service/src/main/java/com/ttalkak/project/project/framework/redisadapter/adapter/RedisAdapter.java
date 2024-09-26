package com.ttalkak.project.project.framework.redisadapter.adapter;

import com.ttalkak.project.common.PersistenceAdapter;
import com.ttalkak.project.project.application.outputport.LoadRedidMonitoringOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@PersistenceAdapter
@RequiredArgsConstructor
public class RedisAdapter implements LoadRedidMonitoringOutputPort {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String MONITORING_KEY = "monitoring";
    private static final long MONITORING_EXPIRATION_TIME = 5;

    /**
     * 모니터링 정보 저장
     * @param userId
     * @param monitoringInfo
     */
    public void saveMonitoringData(Long userId, String monitoringInfo) {
        String key = generateMonitoringKey(userId);

        // 모니터링 정보를 user id에 기반한 set에 저장 (monitoring:유저id, 모니터링정보)
        redisTemplate.opsForValue().set(key, monitoringInfo);
        redisTemplate.expire(MONITORING_KEY, MONITORING_EXPIRATION_TIME, TimeUnit.MINUTES);

    }

    /**
     * 모니터링 정보 조회
     * @param userId
     * @return
     */
    public String getMonitoringData(Long userId) {
        String key = generateMonitoringKey(userId);
        Object result = redisTemplate.opsForValue().get(key);
        return (result != null ? (String) result : null);
    }

    private String generateMonitoringKey(Long userId) {
        return MONITORING_KEY + ":" + userId;
    }

}
