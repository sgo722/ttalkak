package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.framework.web.request.DeploymentUpdateStatusRequest;

public interface UpdateDeploymentStatusUsecase {

    void updateDeploymentStatus(DeploymentUpdateStatusRequest deploymentUpdateStatusRequest);
}
