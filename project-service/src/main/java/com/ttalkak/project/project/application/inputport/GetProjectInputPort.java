package com.ttalkak.project.project.application.inputport;

import com.ttalkak.project.common.UseCase;
import com.ttalkak.project.project.application.outputport.DeploymentOutputPort;
import com.ttalkak.project.project.application.outputport.LoadProjectOutputPort;
import com.ttalkak.project.project.application.usecase.GetProjectUseCase;
import com.ttalkak.project.project.domain.model.ProjectEntity;
import com.ttalkak.project.project.framework.web.request.DomainNameRequest;
import com.ttalkak.project.project.framework.web.response.ProjectPageResponse;
import com.ttalkak.project.project.framework.web.response.ProjectResponse;
import com.ttalkak.project.project.framework.web.response.ProjectWebHookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@UseCase
@RequiredArgsConstructor
public class GetProjectInputPort implements GetProjectUseCase {

    private final LoadProjectOutputPort loadProjectOutputPort;

    private final DeploymentOutputPort deploymentOutputPort;

    /**
     * 프로젝트 단건 조회
     * @param projectId
     * @return
     */
    @Override
    public ProjectResponse getProject(Long projectId) {
        ProjectEntity result = loadProjectOutputPort.findById(projectId);
        ProjectResponse projectResponse = ProjectResponse.mapToResponse(result);
        projectResponse.setDeployments(deploymentOutputPort.getDeployments(projectId));
        return projectResponse;
    }

    @Override
    public ProjectResponse getFeignProject(Long projectId) {
        ProjectEntity result = loadProjectOutputPort.findById(projectId);
        ProjectResponse projectResponse = ProjectResponse.mapToResponse(result);
        return projectResponse;
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
            projects = loadProjectOutputPort.findMyPrjectsContinsSearchKeyWord(pageable, userId, searchKeyword);
        }
        Page<ProjectResponse> page = projects.map(ProjectResponse::mapToResponse);

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
     * 모메인명 중복 체크
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
