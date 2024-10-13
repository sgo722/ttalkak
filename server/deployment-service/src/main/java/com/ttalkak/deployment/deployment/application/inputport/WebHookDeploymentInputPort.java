package com.ttalkak.deployment.deployment.application.inputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.outputport.*;
import com.ttalkak.deployment.deployment.application.usecase.WebHookCommand;
import com.ttalkak.deployment.deployment.application.usecase.WebHookDeploymentUseCase;
import com.ttalkak.deployment.deployment.domain.event.*;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.deployment.domain.model.VersionEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.Status;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import com.ttalkak.deployment.deployment.framework.projectadapter.dto.ProjectInfoResponse;
import com.ttalkak.deployment.deployment.framework.projectadapter.dto.ProjectWebHookResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class WebHookDeploymentInputPort implements WebHookDeploymentUseCase {
    private final ProjectOutputPort projectOutputPort;
    private final DeploymentOutputPort deploymentOutputPort;
    private final HostingOutputPort hostingOutputPort;
    private final EventOutputPort eventOutputPort;
    private final VersionOutputPort versionOutputPort;


    @Override
    public void createDeploymentWebHook(ServiceType serviceType, String webhookToken, WebHookCommand command) {
        ProjectWebHookResponse projectWebHookResponse = projectOutputPort.getWebHookProject(webhookToken);

        Long userId = projectWebHookResponse.getUserId();
        DeploymentEntity deployment = deploymentOutputPort.findByProjectIdAndServiceType(projectWebHookResponse.getProjectId(), serviceType).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_EXISTS_DEPLOYMENT)
        );

        if (deployment.getStatus() != Status.RUNNING) return;

        String expirationDate = null;
        try {
            ProjectInfoResponse projectInfo = projectOutputPort.getProjectInfo(deployment.getProjectId());
            expirationDate = projectInfo.getExpirationDate();
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.KAFKA_CREATE_INSTANCE_PRODUCER_ERROR);
        }
        // 배포 상태 변환
        deployment.setStatus(Status.PENDING);

        VersionEntity versionEntity = versionOutputPort.findLastVersionByDeploymentId(deployment.getId());

        Long newVersionId = versionEntity.getVersion() + 1;

        VersionEntity newVersionEntity = VersionEntity.createVersion(deployment, newVersionId, command.getCommitMessage(), command.getCommitUserProfile(), command.getCommitUsername());
        VersionEntity savedVersion = versionOutputPort.save(newVersionEntity);
        deployment.addVersion(savedVersion);


        HostingEntity hosting = hostingOutputPort.findByProjectIdAndServiceType(projectWebHookResponse.getProjectId(), serviceType);

        if(hosting == null){
            throw new BusinessException(ErrorCode.NOT_EXISTS_HOSTING);
        }

        List<EnvEvent> envEvents = deployment.getEnvs().stream().map(env -> new EnvEvent(env.getKey(), env.getValue())).toList();
        HostingEvent hostingEvent = new HostingEvent(
                deployment.getId(),
                hosting.getId(),
                hosting.getHostingPort(),
                hosting.getDeployerId(),
                hosting.getDetailSubDomainName(),
                hosting.getDetailSubDomainKey()
        );

        DeploymentEvent deploymentEvent = new DeploymentEvent(deployment.getId(), deployment.getProjectId(), envEvents, deployment.getServiceType().toString());
        GithubInfoEvent githubInfoEvent = new GithubInfoEvent(deployment.getGithubInfo().getRepositoryUrl(), deployment.getGithubInfo().getRootDirectory(), deployment.getGithubInfo().getBranch());
        CreateInstanceEvent createInstanceEvent = null;
        if (deployment.getDockerfileScript().equals("Docker File Exist")) {
            createInstanceEvent = new CreateInstanceEvent(userId, deploymentEvent, hostingEvent, githubInfoEvent, envEvents, versionEntity.getVersion(), expirationDate, true, deployment.getDockerfileScript());
        } else {
            createInstanceEvent = new CreateInstanceEvent(userId, deploymentEvent, hostingEvent, githubInfoEvent, envEvents, versionEntity.getVersion(), expirationDate, false, deployment.getDockerfileScript());
        }

        try {
            eventOutputPort.occurRebuildInstance(createInstanceEvent);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.KAFKA_CREATE_INSTANCE_PRODUCER_ERROR);
        }
    }
}
