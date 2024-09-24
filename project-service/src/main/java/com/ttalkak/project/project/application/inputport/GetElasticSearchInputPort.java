package com.ttalkak.project.project.application.inputport;

import com.ttalkak.project.common.UseCase;
import com.ttalkak.project.project.application.outputport.LoadElasticSearchOutputPort;
import com.ttalkak.project.project.application.usecase.GetElasticSearchUseCase;
import com.ttalkak.project.project.framework.web.request.SearchLogRequest;
import com.ttalkak.project.project.framework.web.response.LogPageResponse;
import com.ttalkak.project.project.framework.web.response.MonitoringInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient;


@Transactional
@UseCase
@RequiredArgsConstructor
public class GetElasticSearchInputPort implements GetElasticSearchUseCase {

    private final BedrockRuntimeClient client;

    private final String modelId = "anthropic.claude-3-5-sonnet-20240620-v1:0";

    private final LoadElasticSearchOutputPort loadElasticSearchOutputPort;

    /**
     * 페이징 처리한 로그 조회
     * @param searchLogRequest
     * @return
     */
    @Override
    public LogPageResponse getLogsByPageable(SearchLogRequest searchLogRequest) throws Exception {
        return loadElasticSearchOutputPort.getLogsByPageable(searchLogRequest);
    }

    /**
     * AI 모니터링 정보 제공
     * @param deploymentId
     * @return
     * @throws Exception
     */
    @Override
    public MonitoringInfoResponse getAIMonitoringInfo(String deploymentId) throws Exception {
        return loadElasticSearchOutputPort.getAIMonitoringInfo(deploymentId);
    }

}
