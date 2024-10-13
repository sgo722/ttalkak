package com.ttalkak.project.project.framework.web.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AIMonitoringResponse {

    private final MonitoringInfoResponse monitoringInfoResponse;

    private final String answer;

}
