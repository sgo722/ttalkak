package com.ttalkak.project.project.application.inputport;

import com.ttalkak.project.common.UseCase;
import com.ttalkak.project.common.error.ErrorCode;
import com.ttalkak.project.common.exception.BusinessException;
import com.ttalkak.project.project.application.outputport.EventOutputPort;
import com.ttalkak.project.project.application.outputport.LoadProjectOutputPort;
import com.ttalkak.project.project.application.outputport.SaveProjectOutputPort;
import com.ttalkak.project.project.application.usecase.UpdateProjectUseCase;
import com.ttalkak.project.project.domain.event.DomainNameEvent;
import com.ttalkak.project.project.domain.model.vo.ProjectEditor;
import com.ttalkak.project.project.domain.model.ProjectEntity;
import com.ttalkak.project.project.framework.web.request.ProjectUpdateRequest;
import com.ttalkak.project.project.framework.web.response.ProjectDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@UseCase
@RequiredArgsConstructor
public class UpdateProjectInputPort implements UpdateProjectUseCase {

    private final LoadProjectOutputPort loadProjectOutputPort;

    private final SaveProjectOutputPort saveProjectOutputPort;

    private final EventOutputPort eventOutputPort;

    /**
     * 프로젝트 변경
     * @param projectId
     * @param projectUpdateRequest
     * @return
     */
    @Override
    public ProjectDetailResponse updateProject(Long projectId, ProjectUpdateRequest projectUpdateRequest) {
        ProjectEntity projectEntity = loadProjectOutputPort.findById(projectId);

        // 도메인명 중복 체크
        if(projectUpdateRequest.getDomainName() != null && !"".equals(projectUpdateRequest.getDomainName())) {
            ProjectEntity findProjectEntityByDomainName = loadProjectOutputPort.findByDomainName(projectUpdateRequest.getDomainName());

            if(findProjectEntityByDomainName != null && projectEntity.getId() != findProjectEntityByDomainName.getId()) {
                throw new BusinessException(ErrorCode.ALREADY_EXISTS_DOMAIN_NAME);
            }
        }

        String orgDomainName = projectEntity.getDomainName();

        ProjectEditor.ProjectEditorBuilder projectEditorBuilder = projectEntity.toEditor();
        ProjectEditor projectEditor = projectEditorBuilder
                .projectName(projectUpdateRequest.getProjectName())
                .domainName(projectUpdateRequest.getDomainName())
                .expirationDate(projectUpdateRequest.getExpirationDate())
                .build();

        projectEntity.edit(projectEditor);
        saveProjectOutputPort.save(projectEntity);

        // 도메인명 변경을 호스팅에서도 해야하기 때문에 카프카 전송
        if(!orgDomainName.equals(projectUpdateRequest.getDomainName())) {
            eventOutputPort.occurUpdateHostingDomainName(new DomainNameEvent(projectId, orgDomainName, projectUpdateRequest.getDomainName()));
        }

        return ProjectDetailResponse.mapToResponse(projectEntity);
    }

    /**
     * 도메인명 롤백
     * @param domainNameEvent
     */
    @Override
    public void rollbackProjectDomainName(DomainNameEvent domainNameEvent) {
        ProjectEntity projectEntity = loadProjectOutputPort.findById(domainNameEvent.getProjectId());
        ProjectEditor.ProjectEditorBuilder projectEditorBuilder = projectEntity.toEditor();
        ProjectEditor projectEditor = projectEditorBuilder
                .domainName(domainNameEvent.getOrgDomainName())
                .build();

        projectEntity.edit(projectEditor);
        saveProjectOutputPort.save(projectEntity);
    }
}
