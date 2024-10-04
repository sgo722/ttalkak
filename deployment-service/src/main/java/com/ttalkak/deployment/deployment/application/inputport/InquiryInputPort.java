package com.ttalkak.deployment.deployment.application.inputport;

import com.ttalkak.deployment.deployment.application.outputport.DatabaseOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.DeploymentOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.HostingOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.VersionOutputPort;
import com.ttalkak.deployment.deployment.application.usecase.inquiryUseCase;
import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.deployment.domain.model.VersionEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.DeploymentStatus;
import com.ttalkak.deployment.deployment.framework.web.response.DatabaseResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentDetailResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentPreviewResponse;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InquiryInputPort implements inquiryUseCase {

    private final DeploymentOutputPort deploymentOutputPort;
    private final HostingOutputPort hostingOutputPort;
    private final VersionOutputPort versionOutputPort;
    private final DatabaseOutputPort databaseOutputPort;

    // 배포이력 상세조회
    @Override
    public DeploymentDetailResponse getDeployment(Long deploymentId) {
        DeploymentEntity deploymentEntity = deploymentOutputPort.findDeployment(deploymentId)
                // 배포 이력이 존재하지 않은 경우
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_EXISTS_DEPLOYMENT));
        List<VersionEntity> versionEntities = versionOutputPort.findAllByDeploymentId(deploymentEntity);
        HostingEntity hosting = hostingOutputPort.findByProjectIdAndServiceType(deploymentEntity.getProjectId(), deploymentEntity.getServiceType());
        log.info("version entities: {}", versionEntities);
        if(hosting == null){
            return DeploymentDetailResponse.mapToDTO(deploymentEntity, versionEntities);
        }
        return DeploymentDetailResponse.mapToDTO(deploymentEntity, hosting, versionEntities);
    }

    // 프로젝트 관련 배포이력 전체조회
    @Override
    public List<DeploymentPreviewResponse> getDeploymentsByProjectId(Long projectId) {
        List<DeploymentPreviewResponse> collect = deploymentOutputPort.findAllByProjectId(projectId)
                .stream()
                .filter(deployment -> DeploymentStatus.isAlive(deployment.getStatus()))
                .map(DeploymentPreviewResponse::mapToDTO)
                .collect(Collectors.toList());
        log.info("getDeploymentsByProjectId: {} :: {}", projectId, collect);
        return collect;
    }

    // 레포지토리 이름을 포함하면 반
    @Override
    public List<DeploymentPreviewResponse> searchDeploymentByGithubRepositoryName(String githubRepoName, int page, int size) {
        return deploymentOutputPort.searchDeploymentByGithubRepoName(githubRepoName, page, size)
                .stream()
                .filter(deployment -> DeploymentStatus.isAlive(deployment.getStatus()))
                .map(DeploymentPreviewResponse::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DatabaseResponse> getDatabases(Long userId) {
        List<DatabaseEntity> databases = databaseOutputPort.findAllByUserId(userId);
        return databases.stream()
                .map(DatabaseResponse::mapToDTO)
                .toList();
    }

    @Override
    public DatabaseResponse getDatabase(Long databaseId) {
        DatabaseEntity databaseEntity = databaseOutputPort.findById(databaseId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_EXISTS_DATABASE));
        return DatabaseResponse.mapToDTO(databaseEntity);
    }
}
