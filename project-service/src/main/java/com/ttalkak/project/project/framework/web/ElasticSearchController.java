package com.ttalkak.project.project.framework.web;

import com.ttalkak.project.common.ApiResponse;
import com.ttalkak.project.common.WebAdapter;
import com.ttalkak.project.project.application.usecase.GetElasticSearchUseCase;
import com.ttalkak.project.project.domain.model.LogEntryDocument;
import com.ttalkak.project.project.framework.web.request.SearchLogRequest;
import com.ttalkak.project.project.framework.web.response.LogPageResponse;
import com.ttalkak.project.project.framework.web.response.MonitoringInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/v1/log")
public class ElasticSearchController {

    private final GetElasticSearchUseCase getElasticSearchUseCase;

    /**
     * 페이징 처리한 로그 조회
     * @param userId
     * @param searchLogRequest
     * @return
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

    @GetMapping("/test")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MonitoringInfoResponse> getMonitoringInfo() throws Exception {

        MonitoringInfoResponse m = getElasticSearchUseCase.getAIMonitoringInfo("42");
        return ApiResponse.success(m);
    }

}
