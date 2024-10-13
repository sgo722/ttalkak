package com.ttalkak.project.project.framework.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectUpdateRequest {
    String projectName;
    String domainName;
    String expirationDate;

    @Builder
    public ProjectUpdateRequest(String projectName, String domainName, String expirationDate) {
        this.projectName = projectName;
        this.domainName = domainName;
        this.expirationDate = expirationDate;
    }
}
