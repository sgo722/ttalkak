package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;


public interface WebHookDeploymentUseCase {
    void createDeploymentWebHook(ServiceType serviceType, String webhookToken, WebHookCommand command);
}
