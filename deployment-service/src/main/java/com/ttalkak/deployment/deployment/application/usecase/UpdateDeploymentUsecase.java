package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.framework.web.request.DeploymentUpdateRequest;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentDetailResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentPreviewResponse;

public interface UpdateDeploymentUsecase {
    // 배포 정보 수정
    DeploymentDetailResponse updateDeployment(Long userId, DeploymentUpdateRequest deploymentUpdateRequest);
    
}
