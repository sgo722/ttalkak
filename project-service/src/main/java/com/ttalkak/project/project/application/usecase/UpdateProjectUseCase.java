package com.ttalkak.project.project.application.usecase;

import com.ttalkak.project.project.domain.event.DomainNameEvent;
import com.ttalkak.project.project.framework.web.request.ProjectUpdateRequest;
import com.ttalkak.project.project.framework.web.response.ProjectDetailResponse;

public interface UpdateProjectUseCase {
    ProjectDetailResponse updateProject(Long userId, Long projectId, ProjectUpdateRequest projectUpdateRequest);

    void rollbackProjectDomainName(DomainNameEvent domainNameEvent);
}
