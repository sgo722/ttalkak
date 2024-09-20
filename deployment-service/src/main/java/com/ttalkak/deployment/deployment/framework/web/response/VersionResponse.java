package com.ttalkak.deployment.deployment.framework.web.response;

import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.VersionEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
public class VersionResponse {

    private Long id;

    private Long version;

    private String logUrl;

    private String repositoryLastCommitMessage;

    private String repositoryLastCommitUserProfile;

    private String repositoryLastCommitUserName;


    @Builder
    private VersionResponse(Long id, Long version, String logUrl, String repositoryLastCommitMessage, String repositoryLastCommitUserProfile, String repositoryLastCommitUserName) {
        this.id = id;
        this.version = version;
        this.logUrl = logUrl;
        this.repositoryLastCommitMessage = repositoryLastCommitMessage;
        this.repositoryLastCommitUserProfile = repositoryLastCommitUserProfile;
        this.repositoryLastCommitUserName = repositoryLastCommitUserName;
    }

    public static VersionResponse mapToDTO(VersionEntity versionEntity){
        return VersionResponse.builder()
                .id(versionEntity.getId())
                .version(versionEntity.getVersion())
                .logUrl(versionEntity.getLogUrl())
                .repositoryLastCommitMessage(versionEntity.getRepositoryLastCommitMessage())
                .repositoryLastCommitUserName(versionEntity.getRepositoryLastCommitUserName())
                .repositoryLastCommitUserProfile(versionEntity.getRepositoryLastCommitUserProfile())
                .build();
    }
}
