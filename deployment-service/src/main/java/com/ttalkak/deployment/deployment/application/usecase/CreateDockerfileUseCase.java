package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;

public interface CreateDockerfileUseCase {

    String generateDockerfileScript(ServiceType serviceType, String buildTool, String packageManager, String languageVersion);
}
