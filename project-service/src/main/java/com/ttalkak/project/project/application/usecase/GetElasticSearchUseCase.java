package com.ttalkak.project.project.application.usecase;

import com.ttalkak.project.project.framework.web.request.SearchLogRequest;
import com.ttalkak.project.project.framework.web.response.LogPageResponse;
import com.ttalkak.project.project.framework.web.response.MonitoringInfoResponse;

public interface GetElasticSearchUseCase {

    /** 페이징 처리한 로그 조회 */
    LogPageResponse getLogsByPageable(SearchLogRequest searchLogRequest) throws Exception;

    /** AI 모니터링 정보 제공 */
    MonitoringInfoResponse getAIMonitoringInfo(String deploymentId) throws Exception;

}
