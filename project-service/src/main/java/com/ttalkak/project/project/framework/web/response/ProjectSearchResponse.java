package com.ttalkak.project.project.framework.web.response;

import com.ttalkak.project.project.domain.model.ProjectEntity;
import com.ttalkak.project.project.framework.deploymentadapter.dto.DeploymentResponse;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class ProjectSearchResponse {

    private Long id;

    private Long userId;

    private String projectName;

    private String domainName;

    private String webhookToken;

    private String expirationDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    private ProjectSearchResponse(Long id, Long userId, String projectName, String webhookToken, String domainName, String expirationDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.projectName = projectName;
        this.webhookToken = webhookToken;
        this.domainName = domainName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.expirationDate = expirationDate;
    }

    public static ProjectSearchResponse mapToResponse(ProjectEntity projectEntity) {
        return ProjectSearchResponse.builder()
                .id(projectEntity.getId())
                .userId(projectEntity.getUserId())
                .webhookToken(projectEntity.getWebhookToken())
                .projectName(projectEntity.getProjectName())
                .domainName(projectEntity.getDomainName())
                .createdAt(projectEntity.getCreatedAt())
                .updatedAt(projectEntity.getUpdatedAt())
                .expirationDate(projectEntity.getExpirationDate())
                .build();
    }
}
