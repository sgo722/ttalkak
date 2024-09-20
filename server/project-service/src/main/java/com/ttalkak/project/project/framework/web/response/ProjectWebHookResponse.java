package com.ttalkak.project.project.framework.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProjectWebHookResponse {
    private Long projectId;
    private Long userId;
    private String domainName;
}
