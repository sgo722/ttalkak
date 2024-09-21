package com.ttalkak.project.project.framework.web.response;

import lombok.Builder;

import java.util.List;

public class LogPageResponse {
    List<LogResponse> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    @Builder
    public LogPageResponse(List<LogResponse> content, int pageNumber, int pageSize, long totalElements, int totalPages) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
