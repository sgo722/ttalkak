package com.ttalkak.deployment.deployment.framework.projectadapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProjectWebHookResponse {
    private Long projectId;
    private Long userId;
    private String domainName;
}
