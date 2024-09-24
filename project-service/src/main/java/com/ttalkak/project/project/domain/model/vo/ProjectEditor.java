package com.ttalkak.project.project.domain.model.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProjectEditor {
    private final String projectName;
    private final String domainName;
    private final String expirationDate;

    @Builder
    public ProjectEditor(String projectName, String domainName, String expirationDate) {
        this.projectName = projectName;
        this.domainName = domainName;
        this.expirationDate = expirationDate;
    }
}
