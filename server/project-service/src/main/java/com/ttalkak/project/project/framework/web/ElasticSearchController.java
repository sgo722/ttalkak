package com.ttalkak.project.project.framework.web;

import com.ttalkak.project.common.ApiResponse;
import com.ttalkak.project.common.WebAdapter;
import com.ttalkak.project.project.application.usecase.GetElasticSearchUseCase;
import com.ttalkak.project.project.application.usecase.GetLLMUseCase;
import com.ttalkak.project.project.domain.model.LogEntryDocument;
import com.ttalkak.project.project.framework.web.request.SearchLogRequest;
import com.ttalkak.project.project.framework.web.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/v1/log")
public class ElasticSearchController {

    private final GetElasticSearchUseCase getElasticSearchUseCase;
    private final GetLLMUseCase getLLMUseCase;

    /**
     * 페이징 처리한 로그 조회
     * @param userId
     * @param from
     * @param to
     * @param method
     * @param status
     * @param deploymentId
     * @param page
     * @param size
     * @param sort
     * @return
     * @throws Exception
     */
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<LogPageResponse> getLogsByPageable(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) String[] method,
            @RequestParam(required = false) String[] status,
            @RequestParam(required = false) Long deploymentId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "50") Integer size,
            @RequestParam(defaultValue = "desc") String sort) throws Exception {
        SearchLogRequest searchLogRequest = SearchLogRequest.builder()
                .from(from)
                .to(to)
                .method(method)
                .status(status)
                .deploymentId(deploymentId)
                .page(page)
                .size(size)
                .sort(sort)
                .build();

        LogPageResponse pages = getElasticSearchUseCase.getLogsByPageable(searchLogRequest);
        return ApiResponse.success(pages);
    }

    /**
     * 히스토그램 로그 조회
     * @param userId
     * @param from
     * @param to
     * @param deploymentId
     * @return
     * @throws Exception
     */
    @GetMapping("/histogram")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<DashBoardHistogramResponse> getLogHistogram(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) Long deploymentId
    ) throws Exception {

        return ApiResponse.success(getElasticSearchUseCase.getLogHistogram(from, to, deploymentId));
    }

    /**
     * AI 모니터링 정보 조회
     * @param deploymentId
     * @return
     * @throws Exception
     */
    @GetMapping("/monitoring/{deploymentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<AIMonitoringResponse> getMonitoringInfo(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable String deploymentId) throws Exception {
        return ApiResponse.success(getLLMUseCase.getMonitoringInfo(deploymentId));
    }

}
