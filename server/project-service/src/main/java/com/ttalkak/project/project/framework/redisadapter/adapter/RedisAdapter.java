package com.ttalkak.project.project.framework.redisadapter.adapter;

import com.ttalkak.project.common.PersistenceAdapter;
import com.ttalkak.project.project.application.outputport.LoadRedisMonitoringOutputPort;
import com.ttalkak.project.project.domain.model.redis.Monitoring;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@PersistenceAdapter
@RequiredArgsConstructor
public class RedisAdapter implements LoadRedisMonitoringOutputPort {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String MONITORING_KEY = "monitoring";
    private static final long MONITORING_EXPIRATION_TIME = 5;


    /**
     * 모니터링 정보 저장
     * @param userId
     * @param docCount
     * @param llmAnswer
     */
    public void saveMonitoringData(Long userId, long docCount, String llmAnswer) {
        String key = generateMonitoringKey(userId);

        Map<String, Object> monitoringInfo = new HashMap<>();
        monitoringInfo.put("docCount", docCount);
        monitoringInfo.put("answer", llmAnswer);
        monitoringInfo.put("timestamp", Instant.now());

        // 모니터링 정보를 user id에 기반한 set에 저장 (monitoring:유저id, 모니터링정보)
        redisTemplate.opsForHash().putAll(key, monitoringInfo);

    }

    /**
     * 모니터링 정보 조회
     * @param userId
     * @return
     */
    public Monitoring getMonitoringData(Long userId) {
        String key = generateMonitoringKey(userId);
        Map<Object, Object> monitoringInfo = redisTemplate.opsForHash().entries(key);
        if (monitoringInfo.isEmpty()) {
            log.info("캐시된 모니터링 정보 없음 유저: {}", userId);
            return null;
        }
        else {
            return Monitoring.builder()
                    .answer(String.valueOf(monitoringInfo.get("answer")))
                    .docCount(Integer.parseInt(String.valueOf(monitoringInfo.get("docCount"))))
                    .timestamp(Instant.parse((CharSequence) monitoringInfo.get("timestamp")))
                    .build();
        }
    }

    private String generateMonitoringKey(Long userId) {
        return MONITORING_KEY + ":" + userId;
    }

}
