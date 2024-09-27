package com.ttalkak.deployment.deployment.framework.web.response;

import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.deployment.domain.model.VersionEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.DeploymentStatus;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class DeploymentDetailResponse {

    private Long deploymentId;

    private Long projectId;

    @Enumerated(EnumType.STRING)
    private DeploymentStatus status;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    private String repositoryName;

    private String repositoryUrl;

    private String branch;

    private String repositoryOwner;

    private String framework;

    private String payloadURL;

    private List<VersionResponse> versions;

    private List<EnvResponse> envs;

    private HostingResponse hostingResponse;

    private List<DatabaseResponse> databaseResponses;

    private String statusMessage;

    @Builder
    private DeploymentDetailResponse(Long deploymentId,
                                     Long projectId,
                                     DeploymentStatus status,
                                     ServiceType serviceType,
                                     String repositoryName,
                                     String repositoryUrl,
                                     String repositoryOwner,
                                     String branch,
                                     String framework,
                                     String payloadURL,
                                     HostingResponse hostingResponse,
                                     List<VersionResponse> versions,
                                     List<EnvResponse> envs,
                                     List<DatabaseResponse> databaseResponses,
                                     String statusMessage) {
        this.deploymentId = deploymentId;
        this.projectId = projectId;
        this.status = status;
        this.serviceType = serviceType;
        this.repositoryName = repositoryName;
        this.repositoryUrl = repositoryUrl;
        this.repositoryOwner = repositoryOwner;
        this.hostingResponse = hostingResponse;
        this.branch = branch;
        this.envs = envs;
        this.payloadURL = payloadURL;
        this.framework = framework;
        this.versions = versions;
        this.databaseResponses = databaseResponses;
        this.statusMessage = statusMessage;
    }

    public static DeploymentDetailResponse mapToDTO(DeploymentEntity deploymentEntity,
                                                    HostingEntity hostingEntity,
                                                    List<VersionEntity> versionEntities){
        return DeploymentDetailResponse.builder()
                .deploymentId(deploymentEntity.getId())
                .projectId(deploymentEntity.getProjectId())
                .status(deploymentEntity.getStatus())
                .serviceType(deploymentEntity.getServiceType())
                .payloadURL(deploymentEntity.getPayloadURL())
                .repositoryUrl(deploymentEntity.getGithubInfo().getRepositoryUrl())
                .repositoryName(deploymentEntity.getGithubInfo().getRepositoryName())
                .repositoryOwner(deploymentEntity.getGithubInfo().getRepositoryOwner())
                .hostingResponse(HostingResponse.mapToDTO(hostingEntity))
                .envs(deploymentEntity.getEnvs().stream()
                        .map(EnvResponse::mapToDTO)
                        .toList())
                .versions(versionEntities.stream()
                        .map(VersionResponse::mapToDTO)
                        .toList())
                .branch(deploymentEntity.getGithubInfo().getBranch())
                .framework(deploymentEntity.getFramework())
                .databaseResponses(deploymentEntity.getDataBaseEntities().stream()
                        .map(DatabaseResponse::mapToDTO)
                        .toList())
                .statusMessage(deploymentEntity.getStatusMessage())
                .build();
    }
}
