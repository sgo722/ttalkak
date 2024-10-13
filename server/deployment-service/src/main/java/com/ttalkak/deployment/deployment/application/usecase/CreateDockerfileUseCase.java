package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;

public interface CreateDockerfileUseCase {
    public String generateDockerfile(String framework, ServiceType serviceType, String buildTool, String packageManager, String languageVersion);
}
