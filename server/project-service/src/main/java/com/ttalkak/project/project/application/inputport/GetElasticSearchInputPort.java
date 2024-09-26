package com.ttalkak.project.project.application.inputport;

import com.ttalkak.project.common.UseCase;
import com.ttalkak.project.project.application.outputport.LoadElasticSearchOutputPort;
import com.ttalkak.project.project.application.usecase.GetElasticSearchUseCase;
import com.ttalkak.project.project.framework.web.request.SearchLogRequest;
import com.ttalkak.project.project.framework.web.response.DashBoardHistogramResponse;
import com.ttalkak.project.project.framework.web.response.LogHistogramResponse;
import com.ttalkak.project.project.framework.web.response.LogPageResponse;
import com.ttalkak.project.project.framework.web.response.MonitoringInfoResponse;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;


@Transactional
@UseCase
@RequiredArgsConstructor
public class GetElasticSearchInputPort implements GetElasticSearchUseCase {

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
     * 히스토그램 로그 조회
     * @param from
     * @param to
     * @param deploymentId
     * @return
     * @throws Exception
     */
    @Override
    public DashBoardHistogramResponse getLogHistogram(Instant from, Instant to, Long deploymentId) throws Exception {
        Duration duration = Duration.between(from, to);

        DateHistogramInterval interval = switch ((int) duration.toDays()) {
            case 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 -> DateHistogramInterval.hours(6);
            case 5, 6, 7, 8, 9, 10, 11, 12, 13 -> DateHistogramInterval.hours(3);
            case 1, 2, 3, 4 -> DateHistogramInterval.hours(1);
            default -> {
                if(duration.toHours() >= 15) {
                    yield DateHistogramInterval.minutes(30);
                } else {
                    yield DateHistogramInterval.minutes(10);
                }
            }
        };

        return DashBoardHistogramResponse.builder()
                .histograms(loadElasticSearchOutputPort.getLogHistogram(from, to, deploymentId, interval))
                .intervalMinute(duration.toMinutes())
                .build();
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
