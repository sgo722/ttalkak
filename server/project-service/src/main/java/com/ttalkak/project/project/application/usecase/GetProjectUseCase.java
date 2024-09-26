package com.ttalkak.project.project.application.usecase;

import com.ttalkak.project.project.framework.web.request.DomainNameRequest;
import com.ttalkak.project.project.framework.web.response.ProjectPageResponse;
import com.ttalkak.project.project.framework.web.response.ProjectDetailResponse;
import com.ttalkak.project.project.framework.web.response.ProjectWebHookResponse;
import org.springframework.data.domain.Pageable;

public interface GetProjectUseCase {

    ProjectDetailResponse getProject(Long userId, Long projectId);

    ProjectDetailResponse getFeignProject(Long projectId);

    ProjectWebHookResponse getWebHookProject(String webhookToken);

    ProjectPageResponse getProjects(Pageable pageable, String searchKeyword, Long userId);

    Boolean isDuplicateDomainName(DomainNameRequest domainNameRequest);
}
