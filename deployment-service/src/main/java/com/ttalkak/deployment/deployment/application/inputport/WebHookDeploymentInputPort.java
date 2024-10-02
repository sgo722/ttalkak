package com.ttalkak.deployment.deployment.application.inputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.outputport.*;
import com.ttalkak.deployment.deployment.application.usecase.WebHookCommand;
import com.ttalkak.deployment.deployment.application.usecase.WebHookDeploymentUsecase;
import com.ttalkak.deployment.deployment.domain.event.*;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.deployment.domain.model.VersionEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.DeploymentStatus;
import com.ttalkak.deployment.deployment.domain.model.vo.GithubInfo;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import com.ttalkak.deployment.deployment.framework.projectadapter.dto.ProjectInfoResponse;
import com.ttalkak.deployment.deployment.framework.projectadapter.dto.ProjectWebHookResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UseCase
@RequiredArgsConstructor
public class WebHookDeploymentInputPort implements WebHookDeploymentUsecase {
    private final ProjectOutputPort projectOutputPort;
    private final DeploymentOutputPort deploymentOutputPort;
    private final HostingOutputPort hostingOutputPort;
    private final EventOutputPort eventOutputPort;
    private final VersionOutputPort versionOutputPort;


    @Override
    public void createDeploymentWebHook(ServiceType serviceType, String webhookToken, WebHookCommand command) {
        ProjectWebHookResponse response = projectOutputPort.getWebHookProject(webhookToken);

        DeploymentEntity deployment = deploymentOutputPort.findByProjectIdAndServiceType(response.getProjectId(), serviceType).orElseThrow(
                () -> new BusinessException(ErrorCode.NOT_EXISTS_DEPLOYMENT)
        );

        String expirationDate = null;
        try {
            ProjectInfoResponse projectInfo = projectOutputPort.getProjectInfo(deployment.getProjectId());
            expirationDate = projectInfo.getExpirationDate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 배포 상태 변환
        deployment.setStatus(DeploymentStatus.PENDING);

        VersionEntity versionEntity = versionOutputPort.findLastVersionByDeploymentId(deployment.getId());

        Long newVersionId = versionEntity.getVersion() + 1;

        VersionEntity newVersionEntity = VersionEntity.createVersion(deployment, newVersionId, command.getCommitMessage(), command.getCommitUserProfile(), command.getCommitUsername());
        VersionEntity savedVersion = versionOutputPort.save(newVersionEntity);
        deployment.addVersion(savedVersion);


        HostingEntity hosting = hostingOutputPort.findByProjectIdAndServiceType(response.getProjectId(), serviceType);

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

        List<DatabaseEvent> databaseEvents = deployment.getDataBaseEntities().stream()
                .map(databaseEntity -> new DatabaseEvent(databaseEntity.getId(), databaseEntity.getName(), databaseEntity.getDatabaseType().toString(), databaseEntity.getUsername(), databaseEntity.getPassword(), databaseEntity.getPort()))
                .collect(Collectors.toList());

        DeploymentEvent deploymentEvent = new DeploymentEvent(deployment.getId(), deployment.getProjectId(), envEvents, deployment.getServiceType().toString());
        GithubInfoEvent githubInfoEvent = new GithubInfoEvent(deployment.getGithubInfo().getRepositoryUrl(), deployment.getGithubInfo().getRootDirectory(), deployment.getGithubInfo().getBranch());
        CreateInstanceEvent createInstanceEvent = null;
        if(deployment.getDockerfileScript().equals("Docker File Exist")){
            createInstanceEvent = new CreateInstanceEvent(deploymentEvent, hostingEvent, githubInfoEvent, envEvents, databaseEvents, versionEntity.getVersion(), expirationDate, true, deployment.getDockerfileScript());
        }else{
            createInstanceEvent = new CreateInstanceEvent(deploymentEvent, hostingEvent, githubInfoEvent, envEvents, databaseEvents, versionEntity.getVersion(), expirationDate, false, deployment.getDockerfileScript());
        }

        try {
            eventOutputPort.occurRebuildInstance(createInstanceEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카프카 요청 오류가 발생했습니다.");
        }
    }
}
