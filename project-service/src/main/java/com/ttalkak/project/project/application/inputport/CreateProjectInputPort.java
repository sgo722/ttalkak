package com.ttalkak.project.project.application.inputport;

import com.ttalkak.project.common.UseCase;
import com.ttalkak.project.common.error.ErrorCode;
import com.ttalkak.project.common.exception.BusinessException;
import com.ttalkak.project.project.application.outputport.LoadProjectOutputPort;
import com.ttalkak.project.project.application.outputport.SaveProjectOutputPort;
import com.ttalkak.project.project.application.usecase.CreateProjectUseCase;
import com.ttalkak.project.project.domain.model.ProjectEntity;
import com.ttalkak.project.project.framework.web.request.ProjectCreateRequest;
import com.ttalkak.project.project.framework.web.response.ProjectCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@UseCase
@RequiredArgsConstructor
public class CreateProjectInputPort implements CreateProjectUseCase {

    private final SaveProjectOutputPort saveProjectOutputPort;

    private final LoadProjectOutputPort loadProjectOutputPort;

    /**
     * 프로젝트 생성
     * @param projectCreateRequest
     * @return
     */
    @Override
    public ProjectCreateResponse createProject(Long userId, ProjectCreateRequest projectCreateRequest) {

        // 도메인명 중복 체크
        if(projectCreateRequest.getDomainName() != null && !"".equals(projectCreateRequest.getDomainName())) {
            ProjectEntity projectEntity = loadProjectOutputPort.findByDomainName(projectCreateRequest.getDomainName());
            if(projectEntity != null) {
                throw new BusinessException(ErrorCode.ALREADY_EXISTS_DOMAIN_NAME);
            }
        }

        ProjectEntity projectEntity = ProjectEntity.builder()
                .projectName(projectCreateRequest.getProjectName())
                .domainName(projectCreateRequest.getDomainName())
                .userId(userId)
                .expirationDate(projectCreateRequest.getExpirationDate())
                .build();

        ProjectEntity result = saveProjectOutputPort.save(projectEntity);

        return ProjectCreateResponse.mapToResponse(result);
    }
}
