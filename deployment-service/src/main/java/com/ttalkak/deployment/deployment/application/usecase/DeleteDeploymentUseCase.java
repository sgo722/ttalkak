package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.common.UseCase;


@UseCase
public interface DeleteDeploymentUseCase {
    void deleteDeployment(Long userId, Long deploymentId);
    
    public void deleteDeploymentByProject(Long projectId) throws Exception;
}
