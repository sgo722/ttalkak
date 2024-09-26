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
        Duration between;
        DateHistogramInterval interval = switch ((int) duration.toDays()) {
            // 14~30 일 -> 6시간
            case 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 -> {
                between = Duration.ofHours(6);
                yield DateHistogramInterval.hours(6);
            }
            // 5~ 13일 -> 3시간
            case 5, 6, 7, 8, 9, 10, 11, 12, 13 -> {
                between = Duration.ofHours(3);
                yield DateHistogramInterval.hours(3);
            }
            // 1 ~ 4일 -> 1시간
            case 1, 2, 3, 4 -> {
                between = Duration.ofHours(1);
                yield DateHistogramInterval.hours(1);
            }
            // 15시간 이상 23:59 -> 30분
            default -> {
                if(duration.toHours() >= 15) {
                    between = Duration.ofMinutes(30);
                    yield DateHistogramInterval.minutes(30);
                } else {
                    
                    // 10분
                    between = Duration.ofMinutes(10);
                    yield DateHistogramInterval.minutes(10);
                }
            }
        };


        return DashBoardHistogramResponse.builder()
                .histograms(loadElasticSearchOutputPort.getLogHistogram(from, to, deploymentId, interval))
                .intervalMinute(between.toMinutes())
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
