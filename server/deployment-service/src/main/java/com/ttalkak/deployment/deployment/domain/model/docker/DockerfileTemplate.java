package com.ttalkak.deployment.deployment.domain.model.docker;

import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;

public abstract class DockerfileTemplate {

    public final String generateDockerfileScript(ServiceType serviceType, String buildTool, String packageManager, String languageVersion) {
        StringBuilder dockerfileBuilder = new StringBuilder();

        dockerfileBuilder.append(setupBaseImage(buildTool, languageVersion));
        dockerfileBuilder.append(setupWorkdir());
        dockerfileBuilder.append(copyFiles());
        dockerfileBuilder.append(runBuildCommand(buildTool, packageManager));
        dockerfileBuilder.append(setupFinalStage(serviceType, buildTool, languageVersion));

        return dockerfileBuilder.toString();
    }

    protected abstract String setupBaseImage(String buildTool, String languageVersion);
    protected abstract String runBuildCommand(String buildTool, String packageManager);
    protected abstract String setupFinalStage(ServiceType serviceType, String buildTool, String languageVersion);

    protected String setupWorkdir() {
        return "WORKDIR /app\n";
    }

    protected String copyFiles() {
        return "COPY . .\n";
    }
}
