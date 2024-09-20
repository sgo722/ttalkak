package com.ttalkak.deployment.deployment.framework.projectadapter.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProjectInfoResponse {
    private Long userId;
    private String domainName;
    private String webhookToken;
    private String expirationDate;
}
