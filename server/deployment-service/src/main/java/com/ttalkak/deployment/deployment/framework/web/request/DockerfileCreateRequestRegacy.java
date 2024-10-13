package com.ttalkak.deployment.deployment.framework.web.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DockerfileCreateRequestRegacy {

    private String buildEnv;

    private String gitTree;

    private String owner;

    private String repo;

    private String rootDirectory;

    private String branch;
}