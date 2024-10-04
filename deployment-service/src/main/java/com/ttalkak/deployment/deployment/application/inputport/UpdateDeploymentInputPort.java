package com.ttalkak.deployment.deployment.application.inputport;

import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.outputport.DeploymentOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.DomainOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.HostingOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.ProjectOutputPort;
import com.ttalkak.deployment.deployment.application.usecase.UpdateDeploymentUsecase;
import com.ttalkak.deployment.deployment.domain.event.EnvEvent;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.EnvEntity;
import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.DatabaseEditor;
import com.ttalkak.deployment.deployment.domain.model.vo.DeploymentEditor;
import com.ttalkak.deployment.deployment.domain.model.vo.GithubInfo;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.WebDomainRequest;
import com.ttalkak.deployment.deployment.framework.projectadapter.dto.ProjectInfoResponse;
import com.ttalkak.deployment.deployment.framework.web.request.DatabaseUpdateRequest;
import com.ttalkak.deployment.deployment.framework.web.request.DeploymentUpdateRequest;
import com.ttalkak.deployment.deployment.framework.web.request.EnvUpdateRequest;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentDetailResponse;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class UpdateDeploymentInputPort implements UpdateDeploymentUsecase {

    private final DeploymentOutputPort deploymentOutputPort;

    private final ProjectOutputPort projectOutputPort;

    private final EnvOutputPort envOutputPort;

    private final HostingOutputPort hostingOutputPort;

    private final DomainOutputPort domainOutputPort;

    @Override
    public DeploymentDetailResponse updateDeployment(Long userId, DeploymentUpdateRequest deploymentUpdateRequest) {

        DeploymentEntity deploymentEntity = deploymentOutputPort.findDeployment(deploymentUpdateRequest.getDeploymentId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_EXISTS_DEPLOYMENT));

        ProjectInfoResponse projectInfo = projectOutputPort.getProjectInfo(deploymentEntity.getProjectId());
        if(!Objects.equals(userId, projectInfo.getUserId())){
            throw new EntityNotFoundException(ErrorCode.UN_AUTHORIZATION);
        }

        // 깃허브 관련 정보 객체 생성
        GithubInfo newGithubInfo = GithubInfo.create(
                deploymentUpdateRequest.getGithubRepositoryRequest().getRepositoryOwner(),
                deploymentUpdateRequest.getGithubRepositoryRequest().getRepositoryName(),
                deploymentUpdateRequest.getGithubRepositoryRequest().getRepositoryUrl(),
                deploymentUpdateRequest.getGithubRepositoryRequest().getRootDirectory(),
                deploymentUpdateRequest.getGithubRepositoryRequest().getBranch()
        );

        // 프로젝트의 도메인명 가져오기
        String domainName = projectInfo.getDomainName();

        // 호스팅 정보 수정
        HostingEntity hosting = hostingOutputPort.findByProjectIdAndServiceType(deploymentEntity.getProjectId(), deploymentEntity.getServiceType());
        if(hosting == null){
            throw new BusinessException(ErrorCode.NOT_EXISTS_HOSTING);
        }
        hosting.setHostingPort(deploymentUpdateRequest.getHostingPort());
        hosting.updateDomainName(domainName, String.valueOf(deploymentEntity.getServiceType()));

        // 업데이트된 내용의 데이터베이스
        List<DatabaseUpdateRequest> updatedDatabases = deploymentUpdateRequest.getDatabaseUpdateRequests();


        // Env 데이터 수정
        List<EnvEvent> envs = new ArrayList<>();
        deploymentEntity.getEnvs().clear();
        for(EnvUpdateRequest envUpdateRequest : deploymentUpdateRequest.getEnvs()){
            EnvEntity env = EnvEntity.create(envUpdateRequest.getKey(), envUpdateRequest.getValue(), deploymentEntity);
            EnvEntity savedEnv = envOutputPort.save(env);
            deploymentEntity.createEnv(savedEnv);
            envs.add(new EnvEvent(envUpdateRequest.getKey(), envUpdateRequest.getValue()));
        }

        // 배포 객체 수정
        DeploymentEditor.DeploymentEditorBuilder deploymentEditorBuilder = deploymentEntity.toEditor();

        DeploymentEditor deploymentEditor = deploymentEditorBuilder.githubInfo(newGithubInfo)
                .envs(deploymentEntity.getEnvs())
                .build();

        deploymentEntity.edit(deploymentEditor);

        DeploymentEntity savedDeployment = deploymentOutputPort.save(deploymentEntity);

        // 도메인 이름 수정
        domainOutputPort.updateDomainKey(new WebDomainRequest(

                hosting.getId().toString(),
                projectInfo.getDomainName() + " " + hosting.getServiceType().toString(),
                hosting.getDetailSubDomainName()
                ));
        // TO Do List
        return DeploymentDetailResponse.mapToDTO(savedDeployment, hosting, deploymentEntity.getVersions());
    }
}
