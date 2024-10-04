package com.ttalkak.deployment.deployment.application.usecase;


import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.deployment.framework.web.response.DatabasePageResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DatabaseResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentDetailResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentPreviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface inquiryUseCase {
    DeploymentDetailResponse getDeployment(Long deploymentId);
    List<DeploymentPreviewResponse> getDeploymentsByProjectId(Long projectId);
    List<DeploymentPreviewResponse> searchDeploymentByGithubRepositoryName(String githubRepoName, int page, int size);
    DatabasePageResponse getDatabases(Pageable pageable, String searchKeyword, Long userId);
    DatabaseResponse getDatabase(Long databaseId);
}
