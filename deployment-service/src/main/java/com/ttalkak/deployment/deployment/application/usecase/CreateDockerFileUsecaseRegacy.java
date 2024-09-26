package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.framework.web.request.DockerfileCreateRequestRegacy;
import com.ttalkak.deployment.deployment.framework.web.response.DockerFileResponse;

public interface CreateDockerFileUsecaseRegacy {

    DockerFileResponse createDockerFile(Long userId, DockerfileCreateRequestRegacy dockerfileCreateRequestRegacy);
}
