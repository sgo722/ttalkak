package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.deployment.framework.web.request.DeploymentUpdateRequest;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentDetailResponse;


public interface UpdateDeploymentUseCase {
    // 배포 정보 수정
    DeploymentDetailResponse updateDeployment(Long userId, DeploymentUpdateRequest deploymentUpdateRequest);
    
}
