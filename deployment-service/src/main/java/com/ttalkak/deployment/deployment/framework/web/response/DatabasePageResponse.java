package com.ttalkak.deployment.deployment.framework.web.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DatabasePageResponse {

    private List<DatabasePreviewResponse> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;


    @Builder
    public DatabasePageResponse(List<DatabasePreviewResponse> content, int pageNumber, int pageSize, long totalElements, int totalPages) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
