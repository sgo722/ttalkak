package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.deployment.framework.web.request.DeploymentCreateRequest;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentCreateResponse;

public interface CreateDeploymentUseCase {

    DeploymentCreateResponse createDeployment(DeploymentCreateRequest deploymentCreateRequest);
}
