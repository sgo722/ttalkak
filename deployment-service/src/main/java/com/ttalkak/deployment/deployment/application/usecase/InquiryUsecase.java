package com.ttalkak.deployment.deployment.application.usecase;


import com.ttalkak.deployment.deployment.framework.web.response.DeploymentDetailResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentPreviewResponse;
import java.util.List;

public interface InquiryUsecase {
    public DeploymentDetailResponse getDeployment(Long deploymentId);
    public List<DeploymentPreviewResponse> getDeploymentsByProjectId(Long projectId);
    public List<DeploymentPreviewResponse> searchDeploymentByGithubRepositoryName(String githubRepoName, int page, int size);
}
