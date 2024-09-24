package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.framework.web.request.DeploymentCreateRequest;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentCreateResponse;

public interface CreateDeploymentUsecase {

    DeploymentCreateResponse createDeployment(DeploymentCreateRequest deploymentCreateRequest);
}
