package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;

public interface CreateDockerfileUseCase {
    public String generateDockerfile(ServiceType serviceType, String buildTool, String packageManager, String languageVersion);
}
