package com.ttalkak.deployment.deployment.framework.web.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DockerfileCreateRequest {

    private String buildEnv;

    private String gitTree;
}
