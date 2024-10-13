package com.ttalkak.deployment.deployment.framework.web.request;

import com.ttalkak.deployment.deployment.domain.event.CommandEvent;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeploymentCommandStatusRequest {

    private Long deploymentId;

    @Enumerated(EnumType.STRING)
    private CommandEvent command;

    @Builder
    public DeploymentCommandStatusRequest(Long deploymentId, CommandEvent command) {
        this.deploymentId = deploymentId;
        this.command = command;
    }
}