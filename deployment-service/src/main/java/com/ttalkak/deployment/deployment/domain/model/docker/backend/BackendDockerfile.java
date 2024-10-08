package com.ttalkak.deployment.deployment.domain.model.docker.backend;

import com.ttalkak.deployment.deployment.domain.model.docker.DockerfileTemplate;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;

public class BackendDockerfile extends DockerfileTemplate {

    private final BuildToolStrategy buildToolStrategy;

    public BackendDockerfile(BuildToolStrategy buildToolStrategy) {
        this.buildToolStrategy = buildToolStrategy;
    }

    @Override
    protected String setupBaseImage(String buildTool, String languageVersion) {
        return buildToolStrategy.setupBaseImage(languageVersion);
    }

    @Override
    protected String runBuildCommand(String buildTool, String packageManager) {
        return buildToolStrategy.runBuildCommand();
    }

    @Override
    protected String setupFinalStage(ServiceType serviceType, String buildTool, String languageVersion) {
        StringBuilder finalStage = new StringBuilder();
        finalStage.append("FROM eclipse-temurin:").append(languageVersion).append("-jdk\n")
                .append("WORKDIR /app\n")
                .append(buildToolStrategy.copyArtifact())
                .append("ENTRYPOINT [\"java\", \"-jar\", \"/app/app.jar\"]\n");

        return finalStage.toString();
    }
}
