package com.ttalkak.project.project.application.usecase;

import com.ttalkak.project.project.framework.web.request.ProjectCreateRequest;
import com.ttalkak.project.project.framework.web.response.ProjectCreateResponse;

public interface CreateProjectUseCase {

    ProjectCreateResponse createProject(Long userId, ProjectCreateRequest projectCreateRequest);
}
