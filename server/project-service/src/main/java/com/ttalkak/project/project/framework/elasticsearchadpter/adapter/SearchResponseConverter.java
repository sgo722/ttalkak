package com.ttalkak.project.project.framework.elasticsearchadpter.adapter;

import com.ttalkak.project.project.framework.web.response.LogHistogramResponse;
import com.ttalkak.project.project.framework.web.response.MonitoringInfoResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.ParsedFilters;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;

import java.util.ArrayList;
import java.util.List;

public class SearchResponseConverter {

    public static List<LogHistogramResponse> toLogHistogramResponse(SearchResponse searchResponse) {
        List<LogHistogramResponse> response = new ArrayList<>();

        ParsedDateHistogram aggCount = searchResponse.getAggregations().get("daily_request_count");
        for (Histogram.Bucket bucket : aggCount.getBuckets()) {
            String timestamp = bucket.getKeyAsString();
            long docCount = bucket.getDocCount();

            response.add(LogHistogramResponse.builder()
                    .timestamp(timestamp)
                    .docCount(docCount)
                    .build());
        }

        return response;
    }

    public static MonitoringInfoResponse toMonitoringInfoResponse (SearchResponse searchResponse) {

        long totalDocCount = 0L;
        SearchHits hits = searchResponse.getHits();
        totalDocCount = hits.getTotalHits().value;

        // ip 호출 집계쿼리
        ParsedStringTerms topIpsAgg = searchResponse.getAggregations()
                .get("top_ips");
        List<MonitoringInfoResponse.AccessIpInfo> accessIpInfoList = new ArrayList<>();
        for (Terms.Bucket bucket : topIpsAgg.getBuckets()) {
            String ip = bucket.getKeyAsString();
            long count = bucket.getDocCount();

            accessIpInfoList.add(MonitoringInfoResponse.AccessIpInfo.builder()
                    .ip(ip)
                    .count(count)
                    .build());
        }

        // 메서드 사용 빈도 집계쿼리
        ParsedStringTerms methodUsageAgg = searchResponse.getAggregations()
                .get("method_usage");
        List<MonitoringInfoResponse.UsedMethodInfo> usedMethodInfoList = new ArrayList<>();
        for (Terms.Bucket bucket : methodUsageAgg.getBuckets()) {
            String method = bucket.getKeyAsString();
            long count = bucket.getDocCount();

            usedMethodInfoList.add(MonitoringInfoResponse.UsedMethodInfo
                    .builder()
                    .method(method)
                    .count(count)
                    .build());
        }

        // 에러 집계쿼리
        long totalErrors = 0;
        ParsedFilters errorTypeAgg = searchResponse.getAggregations()
                .get("error_types");
        List<MonitoringInfoResponse.ErrorCategory> errorCategories = new ArrayList<>();

        for (Filters.Bucket bucket : errorTypeAgg.getBuckets()) {
            String category = bucket.getKeyAsString();
            long bucketDocCount = bucket.getDocCount();
            totalErrors += bucketDocCount;

            List<MonitoringInfoResponse.ErrorPath> errorPaths = new ArrayList<>();

            ParsedTerms topPathsAgg = bucket.getAggregations()
                    .get("top_paths");
            for (Terms.Bucket pathBucket : topPathsAgg.getBuckets()) {

                MonitoringInfoResponse.ErrorPath errorPath = MonitoringInfoResponse.ErrorPath.builder()
                        .path(pathBucket.getKeyAsString())
                        .count(pathBucket.getDocCount())
                        .build();

                errorPaths.add(errorPath);
            }

            MonitoringInfoResponse.ErrorCategory errorCategory = MonitoringInfoResponse.ErrorCategory.builder()
                    .category(category)
                    .count(bucketDocCount)
                    .topPaths(errorPaths)
                    .build();

            errorCategories.add(errorCategory);
        }

        // 평균 응답시간
        ParsedAvg avgResponseAgg = searchResponse.getAggregations()
                .get("avg_response_time");
        double avgResponseTime = 0;
        if (avgResponseAgg != null) {
            avgResponseTime = avgResponseAgg.getValue();
        }


        // 반환값
        MonitoringInfoResponse response = MonitoringInfoResponse.builder()
                .totalDocCount(totalDocCount)
                .avgResponseTime(avgResponseTime)
                .totalErrors(totalErrors)
                .accessIpInfos(accessIpInfoList)
                .usedMethodInfos(usedMethodInfoList)
                .errorCategories(errorCategories)
                .build();

        return response;
    }
}
