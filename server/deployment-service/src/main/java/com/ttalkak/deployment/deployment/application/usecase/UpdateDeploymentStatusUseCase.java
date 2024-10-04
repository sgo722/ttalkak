package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.deployment.framework.web.request.DeploymentUpdateStatusRequest;


@UseCase
public interface UpdateDeploymentStatusUseCase {

    void updateDeploymentStatus(DeploymentUpdateStatusRequest deploymentUpdateStatusRequest);
}
