package com.ttalkak.project.project.application.usecase;

import com.ttalkak.project.project.framework.web.response.AIMonitoringResponse;

public interface GetLLMUseCase {

    /** AI 모니터링 정보 조회 */
    public AIMonitoringResponse getMonitoringInfo(String deploymentId) throws Exception;

}
