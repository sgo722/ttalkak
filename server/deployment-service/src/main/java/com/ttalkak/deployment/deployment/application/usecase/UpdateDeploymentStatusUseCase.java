package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.framework.web.request.UpdateStatusRequest;


public interface UpdateDeploymentStatusUseCase {

    void updateDeploymentStatus(UpdateStatusRequest updateStatusRequest);
}
