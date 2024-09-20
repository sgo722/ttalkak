package com.ttalkak.project.project.application.outputport;

import com.ttalkak.project.project.domain.model.LogEntryDocument;
import com.ttalkak.project.project.framework.web.request.SearchLogRequest;
import com.ttalkak.project.project.framework.web.response.LogPageResponse;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.List;

public interface LoadElasticSearchOutputPort {

    /** 페이징 처리한 로그 조회 */
    List<LogEntryDocument> getLogsByPageable(SearchLogRequest searchLogRequest) throws IOException;
}
