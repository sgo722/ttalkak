package com.ttalkak.deployment.deployment.framework.web.request;

import com.ttalkak.deployment.deployment.domain.model.vo.DeploymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class DeploymentUpdateStatusRequest {

    private String deploymentId;

    @Enumerated(EnumType.STRING)
    private DeploymentStatus status;

    private String message;
}
