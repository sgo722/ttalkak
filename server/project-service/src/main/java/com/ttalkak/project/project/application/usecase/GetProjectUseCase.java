package com.ttalkak.project.project.application.usecase;

import com.ttalkak.project.project.framework.web.request.DomainNameRequest;
import com.ttalkak.project.project.framework.web.response.ProjectPageResponse;
import com.ttalkak.project.project.framework.web.response.ProjectResponse;
import com.ttalkak.project.project.framework.web.response.ProjectWebHookResponse;
import org.springframework.data.domain.Pageable;

public interface GetProjectUseCase {

    ProjectResponse getProject(Long projectId);

    ProjectResponse getFeignProject(Long projectId);

    ProjectWebHookResponse getWebHookProject(String webhookToken);

    ProjectPageResponse getProjects(Pageable pageable, String searchKeyword, Long userId);

    Boolean isDuplicateDomainName(DomainNameRequest domainNameRequest);
}
