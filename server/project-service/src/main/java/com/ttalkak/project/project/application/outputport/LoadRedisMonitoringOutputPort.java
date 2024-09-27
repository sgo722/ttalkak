package com.ttalkak.project.project.application.outputport;

import com.ttalkak.project.project.domain.model.redis.Monitoring;

public interface LoadRedisMonitoringOutputPort {

    public void saveMonitoringData(Long userId, long docCount, String llmAnswer);

    public Monitoring getMonitoringData(Long userId);
}
