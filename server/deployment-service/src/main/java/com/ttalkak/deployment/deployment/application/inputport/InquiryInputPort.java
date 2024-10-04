package com.ttalkak.deployment.deployment.application.inputport;

import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.outputport.DatabaseOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.DeploymentOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.HostingOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.VersionOutputPort;
import com.ttalkak.deployment.deployment.application.usecase.inquiryUseCase;
import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.deployment.domain.model.VersionEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.Status;
import com.ttalkak.deployment.deployment.framework.web.response.DatabasePageResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DatabaseResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentDetailResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentPreviewResponse;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                .filter(deployment -> Status.isAlive(deployment.getStatus()))
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
                .filter(deployment -> Status.isAlive(deployment.getStatus()))
                .map(DeploymentPreviewResponse::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public DatabaseResponse getDatabase(Long databaseId) {
        DatabaseEntity databaseEntity = databaseOutputPort.findById(databaseId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_EXISTS_DATABASE));
        return DatabaseResponse.mapToDTO(databaseEntity);
    }


    /**
     * 프로젝트 페이징 조회
     * @param pageable
     * @return
     */
    @Override
    public DatabasePageResponse getDatabases(Pageable pageable, String searchKeyword, Long userId) {
        Page<DatabaseEntity> databases = null;
        if(searchKeyword == null || searchKeyword.isEmpty()) {
            databases = databaseOutputPort.findAllByPaging(pageable, userId);
        } else {
            databases = databaseOutputPort.findDatabaseContainsSearchKeyWord(pageable, userId, searchKeyword);
        }

        // 유저 프로젝트가 아닌 경우 예외 발생
        Page<DatabaseResponse> page = databases.map(DatabaseResponse::mapToDTO);
        if (page.getContent().size() > 0) {
            if( userId != databases.getContent().get(0).getUserId()) {
                throw new BusinessException(ErrorCode.UN_AUTHORIZATION);
            }
        }

        DatabasePageResponse databasePageResponse = DatabasePageResponse.builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return databasePageResponse;

    }
}
