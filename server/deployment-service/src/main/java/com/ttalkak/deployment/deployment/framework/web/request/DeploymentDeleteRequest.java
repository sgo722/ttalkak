package com.ttalkak.deployment.deployment.framework.web.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeploymentDeleteRequest {

    private Long deploymentId;

    private String serviceType;

    private String githubOwner;

    private String githubRepo;
}
