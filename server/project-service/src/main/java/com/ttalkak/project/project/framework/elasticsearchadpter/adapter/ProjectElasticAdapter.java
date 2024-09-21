package com.ttalkak.project.project.framework.elasticsearchadpter.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttalkak.project.common.PersistenceAdapter;
import com.ttalkak.project.project.application.outputport.LoadElasticSearchOutputPort;
import com.ttalkak.project.project.domain.model.LogEntryDocument;
import com.ttalkak.project.project.framework.jpaadapter.repository.LogRepository;
import com.ttalkak.project.project.framework.web.request.SearchLogRequest;
import com.ttalkak.project.project.framework.web.response.LogPageResponse;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@PersistenceAdapter
@RequiredArgsConstructor
public class ProjectElasticAdapter implements LoadElasticSearchOutputPort {

    private final RestHighLevelClient client;

    private final LogRepository logRepository;

    /**
     * 페이징 처리한 로그 조회
     * @param request
     * @return
     * @throws IOException
     */
    @Override
    public List<LogEntryDocument> getLogsByPageable(SearchLogRequest request) throws IOException {

        SearchRequest searchRequest = new SearchRequest("pgrok");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder mainQuery = QueryBuilders.boolQuery();

        // 시간 범위 쿼리 추가
        RangeQueryBuilder timestampRange = QueryBuilders.rangeQuery("@timestamp")
                .gte(request.getFrom())
                .lte(request.getTo());
        mainQuery.must(timestampRange);

        // 상태 쿼리 추가
        if(request.getStatus() != null && request.getStatus().length > 0) {
            BoolQueryBuilder statusQuery = buildStatusQuery(request.getStatus());
            if(statusQuery.hasClauses()) {
                mainQuery.must(statusQuery);
            }
        }

        // HTTP 메소드 쿼리 추가
        if(request.getMethod() != null && request.getMethod().length > 0) {
            BoolQueryBuilder methodQuery = buildMethodQuery(request.getMethod());
            if(methodQuery.hasClauses()) {
                mainQuery.must(methodQuery);
            }
        }

        // deploymentId 쿼리 추가
        if(request.getDeploymentId() != null) {
            mainQuery.must(QueryBuilders.termQuery("deploymentId", request.getDeploymentId()));
        }

        searchSourceBuilder.query(mainQuery);

        // 정렬
        if("desc".equals(request.getSort())) {
            searchSourceBuilder.sort("@timestamp", SortOrder.DESC);
        }

        if("asc".equals(request.getSort())) {
            searchSourceBuilder.sort("@timestamp", SortOrder.ASC);
        }

        // 페이징
        searchSourceBuilder.from(request.getPage());
        searchSourceBuilder.size(request.getSize());

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        return convertSearchResponseToDocuments(searchResponse);
    }

    /** LogEntryDocument 로 캐스팅 */
    private List<LogEntryDocument> convertSearchResponseToDocuments(SearchResponse searchResponse) {
        List<LogEntryDocument> logs = new ArrayList<>();

        for (SearchHit hit : searchResponse.getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            LogEntryDocument logEntryDocument = LogEntryDocument.builder()
                    .id(hit.getId())
                    .timestamp((String) sourceAsMap.get("@timestamp"))
                    .deploymentId((String) sourceAsMap.get("deploymentId"))
                    .ip((String) sourceAsMap.get("ip"))
                    .domain((String) sourceAsMap.get("domain"))
                    .path((String) sourceAsMap.get("path"))
                    .method((String) sourceAsMap.get("method"))
                    .status((String) sourceAsMap.get("status"))
                    .duration((Double) sourceAsMap.get("duration"))
                    .build();

            logs.add(logEntryDocument);
        }

        return logs;
    }

    /** 상태 쿼리 추가 */
    public static BoolQueryBuilder buildStatusQuery(String[] status) {
        BoolQueryBuilder statusQuery = QueryBuilders.boolQuery();

        for(String statusCode : status) {
            statusQuery.should(QueryBuilders.prefixQuery("status", statusCode));
        }

        return statusQuery;
    }

    /** HTTP 메소드 쿼리 추가 */
    public static BoolQueryBuilder buildMethodQuery(String[] methods) {
        BoolQueryBuilder methodQuery = QueryBuilders.boolQuery();
        methodQuery.should(QueryBuilders.termsQuery("method", methods));
        return methodQuery;
    }

}
