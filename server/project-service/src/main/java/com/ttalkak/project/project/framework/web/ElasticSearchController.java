package com.ttalkak.project.project.framework.web;

import com.ttalkak.project.common.ApiResponse;
import com.ttalkak.project.common.WebAdapter;
import com.ttalkak.project.project.application.usecase.GetElasticSearchUseCase;
import com.ttalkak.project.project.domain.model.LogEntryDocument;
import com.ttalkak.project.project.framework.web.request.SearchLogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/elastic")
public class ElasticSearchController {

    private final GetElasticSearchUseCase getElasticSearchUseCase;

    /**
     * 페이징 처리한 로그 조회
     * @param userId
     * @param searchLogRequest
     * @return
     */
    @PostMapping("/log/search")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<LogEntryDocument>> getLogsByPageable(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody SearchLogRequest searchLogRequest) throws IOException {

        List<LogEntryDocument> pages = getElasticSearchUseCase.getLogsByPageable(searchLogRequest);
        return ApiResponse.success(pages);
    }



}
