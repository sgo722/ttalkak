package com.ttalkak.project.project.framework.web.response;

import com.ttalkak.project.project.domain.model.ProjectEntity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class ProjectCreateResponse {

    private Long id;

    private Long userId;

    private String projectName;

    private String domainName;

    private String expirationDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Builder
    public ProjectCreateResponse(Long id, Long userId, String projectName, String domainName, String expirationDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.projectName = projectName;
        this.domainName = domainName;
        this.expirationDate = expirationDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProjectCreateResponse mapToResponse(ProjectEntity projectEntity) {
        return ProjectCreateResponse.builder()
                .id(projectEntity.getId())
                .userId(projectEntity.getUserId())
                .projectName(projectEntity.getProjectName())
                .domainName(projectEntity.getDomainName())
                .expirationDate(projectEntity.getExpirationDate())
                .createdAt(projectEntity.getCreatedAt())
                .updatedAt(projectEntity.getUpdatedAt())
                .build();
    }

}
