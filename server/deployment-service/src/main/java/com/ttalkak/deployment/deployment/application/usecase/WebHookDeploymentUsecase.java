package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;

public interface WebHookDeploymentUsecase {
    void createDeploymentWebHook(ServiceType serviceType, String webhookToken, WebHookCommand command);
}
