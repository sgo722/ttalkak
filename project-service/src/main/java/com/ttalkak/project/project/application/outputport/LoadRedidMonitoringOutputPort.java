package com.ttalkak.project.project.application.outputport;

public interface LoadRedidMonitoringOutputPort {

    public void saveMonitoringData(Long userId, String monitoringInfo);

    public String getMonitoringData(Long userId);
}
