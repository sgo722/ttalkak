package com.ttalkak.project.project.framework.elasticsearchadpter.adapter;

import com.ttalkak.project.common.PersistenceAdapter;
import com.ttalkak.project.project.application.outputport.LoadElasticSearchOutputPort;
import com.ttalkak.project.project.framework.jpaadapter.repository.LogRepository;
import com.ttalkak.project.project.framework.web.request.SearchLogRequest;
import com.ttalkak.project.project.framework.web.response.LogPageResponse;
import com.ttalkak.project.project.framework.web.response.LogResponse;
import com.ttalkak.project.project.framework.web.response.MonitoringInfoResponse;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

import static com.ttalkak.project.project.framework.elasticsearchadpter.adapter.SearchResponseConverter.toMonitoringInfoResponse;

@PersistenceAdapter
@RequiredArgsConstructor
@Transactional
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
    public LogPageResponse getLogsByPageable(SearchLogRequest request) throws IOException {

        SearchRequest searchRequest = new SearchRequest("pgrok");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder mainQuery = QueryBuilders.boolQuery();

        // 시간 범위 쿼리 추가
        RangeQueryBuilder timestampRange = QueryBuilders.rangeQuery("@timestamp")
                .gte(request.getFrom())
                .lte(request.getTo());
        mainQuery.must(timestampRange);

        mainQuery.must(QueryBuilders.termQuery("deploymentId", String.valueOf(request.getDeploymentId())));

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
            mainQuery.must(QueryBuilders.termQuery("deploymentId", String.valueOf(request.getDeploymentId()) ));
        }

        // http status 별 개수 집계쿼리
        FiltersAggregator.KeyedFilter[] statusFilters = getStatusCountFilters();

        // http method 별 개수 집계쿼리
        FiltersAggregator.KeyedFilter[] methodFilters = getMethodCountFilters();

        searchSourceBuilder.query(mainQuery);

        // 집계함수 추가
        searchSourceBuilder.aggregation(AggregationBuilders.filters("status_counts", statusFilters));
        searchSourceBuilder.aggregation(AggregationBuilders.filters("method_counts", methodFilters));

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

    /**
     * AI 모니터링 정보 제공
     * @param deploymentId
     * @return
     * @throws Exception
     */
    @Override
    public MonitoringInfoResponse getAIMonitoringInfo(String deploymentId) throws Exception {
        SearchRequest searchRequest = new SearchRequest("pgrok");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte("now-7d/d")
                        .lte("now/d"))
                .must(QueryBuilders.termQuery("deploymentId", deploymentId));

        sourceBuilder.query(boolQuery);

        FiltersAggregator.KeyedFilter[] filters = new FiltersAggregator.KeyedFilter[]{
                new FiltersAggregator.KeyedFilter("4xx_errors", QueryBuilders.prefixQuery("status", "4")),
                new FiltersAggregator.KeyedFilter("5xx_errors", QueryBuilders.prefixQuery("status", "5"))
        };

        TermsAggregationBuilder topPathsAgg = AggregationBuilders.terms("top_paths")
                .field("path")
                .size(3);

        sourceBuilder.aggregation(
                AggregationBuilders.filters("error_types", filters)
                        .subAggregation(topPathsAgg)

        );

        sourceBuilder.aggregation(
                AggregationBuilders.terms("method_usage")
                        .field("method")
                        .size(3)
        );

        sourceBuilder.aggregation(
                AggregationBuilders.terms("top_ips")
                        .field("ip")
                        .size(3)
        );

        sourceBuilder.aggregation(
                AggregationBuilders.avg("avg_response_time")
                        .field("duration")
        );

        sourceBuilder.size(0); // 집계 결과만 필요하므로 검색 결과는 0개
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        return toMonitoringInfoResponse(searchResponse);
    }

    /** LogEntryDocument 로 캐스팅 */
    private LogPageResponse convertSearchResponseToDocuments(SearchResponse searchResponse) {

        List<LogResponse> logs = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            LogResponse logResponse = LogResponse.builder()
                    .timestamp((String) sourceAsMap.get("@timestamp"))
                    .deploymentId((String) sourceAsMap.get("deploymentId"))
                    .ip((String) sourceAsMap.get("ip"))
                    .domain((String) sourceAsMap.get("domain"))
                    .path((String) sourceAsMap.get("path"))
                    .method((String) sourceAsMap.get("method"))
                    .status((String) sourceAsMap.get("status"))
                    .duration((Double) sourceAsMap.get("duration"))
                    .build();

            logs.add(logResponse);
        }

        Map<String, Long> methodCounts = new HashMap<>();
        Filters methodBuckets = searchResponse.getAggregations().get("method_counts");
        for(Filters.Bucket bucket : methodBuckets.getBuckets()) {
            String method = bucket.getKeyAsString();
            long count = bucket.getDocCount();
            methodCounts.put(method, count);
        }

        Map<String, Long> statusCounts = new HashMap<>();
        Filters statusBuckets = searchResponse.getAggregations().get("status_counts");
        for(Filters.Bucket bucket : statusBuckets.getBuckets()) {
            String status = bucket.getKeyAsString();
            long count = bucket.getDocCount();
            statusCounts.put(status, count);
        }

        return new LogPageResponse(logs, methodCounts, statusCounts);
    }

    /** 상태 쿼리 추가 */
    public BoolQueryBuilder buildStatusQuery(String[] status) {
        BoolQueryBuilder statusQuery = QueryBuilders.boolQuery();

        for(String statusCode : status) {
            statusQuery.should(QueryBuilders.prefixQuery("status", statusCode));
        }
        statusQuery.minimumShouldMatch(1);
        return statusQuery;
    }

    /** HTTP 메소드 쿼리 추가 */
    public BoolQueryBuilder buildMethodQuery(String[] methods) {
        BoolQueryBuilder methodQuery = QueryBuilders.boolQuery();
        methodQuery.should(QueryBuilders.termsQuery("method", methods));
        return methodQuery;
    }

    /** http method 별 개수 집계쿼리 */
    public FiltersAggregator.KeyedFilter[] getMethodCountFilters() {
        return new FiltersAggregator.KeyedFilter[]{
                new FiltersAggregator.KeyedFilter("GET", QueryBuilders.termQuery("method", "GET")),
                new FiltersAggregator.KeyedFilter("POST", QueryBuilders.termQuery("method", "POST")),
                new FiltersAggregator.KeyedFilter("PUT", QueryBuilders.termQuery("method", "PUT")),
                new FiltersAggregator.KeyedFilter("DELETE", QueryBuilders.termQuery("method", "DELETE")),
                new FiltersAggregator.KeyedFilter("PATCH", QueryBuilders.termQuery("method", "PATCH"))
        };
    }

    /** http status 별 개수 집계쿼리 */
    public FiltersAggregator.KeyedFilter[] getStatusCountFilters() {
        return new FiltersAggregator.KeyedFilter[]{
                new FiltersAggregator.KeyedFilter("2xx", QueryBuilders.prefixQuery("status", "2")),
                new FiltersAggregator.KeyedFilter("3xx", QueryBuilders.prefixQuery("status", "3")),
                new FiltersAggregator.KeyedFilter("4xx", QueryBuilders.prefixQuery("status", "4")),
                new FiltersAggregator.KeyedFilter("5xx", QueryBuilders.prefixQuery("status", "5"))
        };
    }

}
