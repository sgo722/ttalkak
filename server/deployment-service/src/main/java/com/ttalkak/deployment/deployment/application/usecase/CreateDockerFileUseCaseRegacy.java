package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.deployment.framework.web.request.DockerfileCreateRequestRegacy;
import com.ttalkak.deployment.deployment.framework.web.response.DockerFileResponse;


@UseCase
public interface CreateDockerFileUseCaseRegacy {

    DockerFileResponse createDockerFile(Long userId, DockerfileCreateRequestRegacy dockerfileCreateRequestRegacy);
}
