package com.ttalkak.project.project.application.usecase;

import com.ttalkak.project.project.domain.event.DomainNameEvent;
import com.ttalkak.project.project.framework.web.request.ProjectUpdateRequest;
import com.ttalkak.project.project.framework.web.response.ProjectResponse;

public interface UpdateProjectUseCase {
    ProjectResponse updateProject(Long projectId, ProjectUpdateRequest projectUpdateRequest);

    void rollbackProjectDomainName(DomainNameEvent domainNameEvent);
}
