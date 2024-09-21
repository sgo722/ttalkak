package com.ttalkak.project.project.framework.web.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectPageResponse {
    private List<ProjectResponse> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    @Builder
    public ProjectPageResponse(List<ProjectResponse> content, int pageNumber, int pageSize, long totalElements, int totalPages) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}
