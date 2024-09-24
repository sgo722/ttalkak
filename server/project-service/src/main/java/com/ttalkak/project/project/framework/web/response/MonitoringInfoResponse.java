package com.ttalkak.project.project.framework.web.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Builder;

import java.util.List;

@Getter
@Builder
public class MonitoringInfoResponse {
    @JsonProperty("totalErrors")
    private final long totalErrors;

    @JsonProperty("avgResponseTime")
    private final double avgResponseTime;

    @JsonProperty("accessIpInfos")
    private final List<AccessIpInfo> accessIpInfos;

    @JsonProperty("usedMethodInfos")
    private final List<UsedMethodInfo> usedMethodInfos;

    @JsonProperty("errorCategories")
    private final List<ErrorCategory> errorCategories;

    @Getter
    @Builder
    public static class AccessIpInfo {
        @JsonProperty("ip")
        private final String ip;

        @JsonProperty("count")
        private final long count;
    }

    @Getter
    @Builder
    public static class UsedMethodInfo {
        @JsonProperty("method")
        private final String method;

        @JsonProperty("count")
        private final long count;
    }

    @Getter
    @Builder
    public static class ErrorCategory {
        @JsonProperty("category")
        private final String category;

        @JsonProperty("count")
        private final long count;

        @JsonProperty("topPaths")
        private final List<ErrorPath> topPaths;
    }

    @Getter
    @Builder
    public static class ErrorPath {
        @JsonProperty("path")
        private final String path;

        @JsonProperty("count")
        private final long count;
    }
}