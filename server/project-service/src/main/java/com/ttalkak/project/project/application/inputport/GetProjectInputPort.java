package com.ttalkak.project.project.application.inputport;

import com.ttalkak.project.common.UseCase;
import com.ttalkak.project.common.error.ErrorCode;
import com.ttalkak.project.common.exception.BusinessException;
import com.ttalkak.project.common.exception.EntityNotFoundException;
import com.ttalkak.project.project.application.outputport.DeploymentOutputPort;
import com.ttalkak.project.project.application.outputport.LoadProjectOutputPort;
import com.ttalkak.project.project.application.usecase.GetProjectUseCase;
import com.ttalkak.project.project.domain.model.ProjectEntity;
import com.ttalkak.project.project.framework.deploymentadapter.dto.DeploymentResponse;
import com.ttalkak.project.project.framework.web.request.DomainNameRequest;
import com.ttalkak.project.project.framework.web.response.ProjectPageResponse;
import com.ttalkak.project.project.framework.web.response.ProjectDetailResponse;
import com.ttalkak.project.project.framework.web.response.ProjectSearchResponse;
import com.ttalkak.project.project.framework.web.response.ProjectWebHookResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@UseCase
@RequiredArgsConstructor
public class GetProjectInputPort implements GetProjectUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetProjectInputPort.class);
    private final LoadProjectOutputPort loadProjectOutputPort;

    private final DeploymentOutputPort deploymentOutputPort;

    /**
     * 프로젝트 단건 조회
     * @param projectId
     * @return
     */
    @Override
    public ProjectDetailResponse getProject(Long userId, Long projectId) {
        ProjectEntity result = loadProjectOutputPort.findById(projectId);

        // 유저 프로젝트가 아닌 경우 예외 발생
        if(result.getUserId() != userId) throw new BusinessException(ErrorCode.ACCESS_PROJECT_DENIED);

        ProjectDetailResponse projectDetailResponse = ProjectDetailResponse.mapToResponse(result);
        List<DeploymentResponse> deployments = deploymentOutputPort.getDeployments(projectId);
        projectDetailResponse.setDeployments(deployments);
        return projectDetailResponse;
    }

    @Override
    public ProjectDetailResponse getFeignProject(Long projectId) {
        ProjectEntity result = loadProjectOutputPort.findById(projectId);
        ProjectDetailResponse projectDetailResponse = ProjectDetailResponse.mapToResponse(result);
        return projectDetailResponse;
    }

    @Override
    public ProjectWebHookResponse getWebHookProject(String webhookToken) {
        ProjectEntity entity = loadProjectOutputPort.findByWebHookToken(webhookToken);
        return ProjectWebHookResponse.builder()
                .projectId(entity.getId())
                .userId(entity.getUserId())
                .domainName(entity.getDomainName())
                .build();
    }

    /**
     * 프로젝트 페이징 조회
     * @param pageable
     * @return
     */
    @Override
    public ProjectPageResponse getProjects(Pageable pageable, String searchKeyword, Long userId) {
        Page<ProjectEntity> projects = null;
        if(searchKeyword == null || searchKeyword.isEmpty()) {
            projects = loadProjectOutputPort.findMyProjects(pageable, userId);
        } else {
            projects = loadProjectOutputPort.findMyProjectsContainsSearchKeyWord(pageable, userId, searchKeyword);
        }

        // 유저 프로젝트가 아닌 경우 예외 발생
        Page<ProjectSearchResponse> page = projects.map(ProjectSearchResponse::mapToResponse);
        if (page.getContent().size() > 0) {
            if( userId != page.getContent().get(0).getUserId()) {
                throw new BusinessException(ErrorCode.ACCESS_PROJECT_DENIED);
            }
        }

        ProjectPageResponse projectPageResponse = ProjectPageResponse.builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();

        return projectPageResponse;
    }

    /**
     * 도메인명 중복 체크
     * @param domainNameRequest
     * @return
     */
    @Override
    public Boolean isDuplicateDomainName(DomainNameRequest domainNameRequest) {
        // 도메인명 중복 체크
        if(domainNameRequest.getDomainName() != null && !"".equals(domainNameRequest.getDomainName())) {
            ProjectEntity projectEntity = loadProjectOutputPort.findByDomainName(domainNameRequest.getDomainName());
            if(projectEntity != null) {
                return true;
            }
        }
        return false;
    }
}
