package com.ttalkak.deployment.deployment.domain.model;

import com.ttalkak.deployment.deployment.domain.model.vo.DatabaseType;
import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class VersionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long version;

    @Setter
    private String logUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deploymentEntity", nullable = false)
    private DeploymentEntity deploymentEntity;

    @Column(nullable = false)
    private String repositoryLastCommitMessage;

    @Column(nullable = false)
    private String repositoryLastCommitUserProfile;

    @Column(nullable = false)
    private String repositoryLastCommitUserName;

    @Builder
    private VersionEntity(Long version, String logUrl, DeploymentEntity deploymentEntity, String repositoryLastCommitMessage, String repositoryLastCommitUserProfile, String repositoryLastCommitUserName) {
        this.version = version;
        this.logUrl = logUrl;
        this.deploymentEntity = deploymentEntity;
        this.repositoryLastCommitMessage = repositoryLastCommitMessage;
        this.repositoryLastCommitUserProfile = repositoryLastCommitUserProfile;
        this.repositoryLastCommitUserName = repositoryLastCommitUserName;
    }


    public static VersionEntity createVersion(DeploymentEntity deploymentEntity, Long versionId, String repositoryLastCommitMessage, String repositoryLastCommitUserProfile, String repositoryLastCommitUserName){
        return VersionEntity.builder()
                .version(versionId)
                .deploymentEntity(deploymentEntity)
                .repositoryLastCommitMessage(repositoryLastCommitMessage)
                .repositoryLastCommitUserName(repositoryLastCommitUserName)
                .repositoryLastCommitUserProfile(repositoryLastCommitUserProfile)
                .logUrl("배포 준비중 입니다.")
                .build();
    }
}
