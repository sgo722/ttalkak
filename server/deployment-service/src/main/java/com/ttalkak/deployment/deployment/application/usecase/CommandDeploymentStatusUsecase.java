package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.framework.web.request.DeploymentCommandStatusRequest;

public interface CommandDeploymentStatusUsecase {

    void commandDeploymentStatus(DeploymentCommandStatusRequest deploymentCommandStatusRequest);
}
