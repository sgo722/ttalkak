package com.ttalkak.project.project.application.outputport;

import com.ttalkak.project.project.domain.model.redis.Monitoring;

public interface LoadRedisMonitoringOutputPort {

    public void saveMonitoringData(String deploymentId, long docCount, String llmAnswer);

    public Monitoring getMonitoringData(String deploymentId);
}
