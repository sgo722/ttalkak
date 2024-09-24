package com.ttalkak.project.project.framework.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchLogRequest {

    private Instant from;
    private Instant to;
    private String[] method; // GET, POST, PUT, PATCH, DELETE
    private String[] status; // 2 3 4 5
    private Long deploymentId;
    private Integer page = 0;
    private Integer size = 50;
    private String sort = "desc";

}
