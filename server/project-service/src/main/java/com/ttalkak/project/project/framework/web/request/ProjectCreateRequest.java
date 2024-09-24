package com.ttalkak.project.project.framework.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class ProjectCreateRequest {

    private String projectName;

    private String domainName;

    private String expirationDate;
}
