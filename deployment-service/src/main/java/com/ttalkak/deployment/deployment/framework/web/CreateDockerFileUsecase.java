package com.ttalkak.deployment.deployment.framework.web;

import com.ttalkak.deployment.deployment.framework.web.request.DockerfileCreateRequest;
import com.ttalkak.deployment.deployment.framework.web.response.DockerFileResponse;

public interface CreateDockerFileUsecase {

    DockerFileResponse createDockerFile(DockerfileCreateRequest dockerfileCreateRequest);
}
