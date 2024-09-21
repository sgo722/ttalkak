package com.ttalkak.project.project.application.inputport;

import com.ttalkak.project.common.UseCase;
import com.ttalkak.project.project.application.outputport.LoadElasticSearchOutputPort;
import com.ttalkak.project.project.application.usecase.GetElasticSearchUseCase;
import com.ttalkak.project.project.domain.model.LogEntryDocument;
import com.ttalkak.project.project.framework.web.request.SearchLogRequest;
import com.ttalkak.project.project.framework.web.response.LogPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
     * @throws IOException
     */
    @Override
    public LogPageResponse getLogsByPageable(SearchLogRequest searchLogRequest) throws IOException {
        return loadElasticSearchOutputPort.getLogsByPageable(searchLogRequest);
    }

}
