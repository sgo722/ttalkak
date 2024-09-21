package com.ttalkak.project.project.domain.model;

import com.ttalkak.project.project.domain.model.vo.ProjectEditor;
import com.ttalkak.project.project.domain.model.vo.ProjectStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@Table(name = "projects")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    private Long userId;

    private String projectName;

    private String domainName;

    private String expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus status = ProjectStatus.ACTIVE;

    @Column(name = "webhook_token", length = 100)
    private String webhookToken;

    @PrePersist
    public void prePersist() {
        this.webhookToken = UUID.randomUUID().toString();
    }

    @Builder
    private ProjectEntity(Long id, Long userId, String projectName, String domainName, ProjectStatus status, String expirationDate) {
        this.id = id;
        this.userId = userId;
        this.projectName = projectName;
        this.domainName = domainName;
        this.expirationDate = expirationDate;
    }

    public ProjectEditor.ProjectEditorBuilder toEditor() {
        return ProjectEditor.builder()
                .projectName(this.projectName)
                .domainName(this.domainName)
                .expirationDate(this.expirationDate);
    }

    public void edit(ProjectEditor projectEditor) {
        this.projectName = projectEditor.getProjectName();
        this.domainName = projectEditor.getDomainName();
        this.expirationDate = projectEditor.getExpirationDate();
    }

    public void rollbackDeletedStatus() {
        this.status = ProjectStatus.ACTIVE;
    }

    public void updateDeletedStatus() {
        this.status = ProjectStatus.DELETED;
    }
}
