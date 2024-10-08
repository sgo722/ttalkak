package com.ttalkak.deployment.deployment.application.inputport;

import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.deployment.application.usecase.CreateDockerfileUseCase;
import com.ttalkak.deployment.deployment.domain.model.docker.*;
import com.ttalkak.deployment.deployment.domain.model.docker.backend.BackendDockerfile;
import com.ttalkak.deployment.deployment.domain.model.docker.backend.GradleBuildToolStrategy;
import com.ttalkak.deployment.deployment.domain.model.docker.backend.MavenBuildToolStrategy;
import com.ttalkak.deployment.deployment.domain.model.docker.frontend.*;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;

@UseCase
public class CreateDockerfileInputPort implements CreateDockerfileUseCase {

    public String generateDockerfile(ServiceType serviceType, String buildTool, String packageManager, String languageVersion) {
        String dockerfileScript = "Dockerfile Not Exist";
        if (ServiceType.isBackendType(serviceType)) {
            DockerfileTemplate backendDockerfile = createBackendDockerfile(buildTool);
            dockerfileScript = backendDockerfile.generateDockerfileScript(
                    serviceType,
                    buildTool,
                    packageManager,
                    languageVersion
            );
        } else if (ServiceType.isFrontendType(serviceType)) {
            DockerfileTemplate frontendDockerfile = createFrontendDockerfile(buildTool, packageManager);
            dockerfileScript = frontendDockerfile.generateDockerfileScript(
                    serviceType,
                    buildTool,
                    packageManager,
                    languageVersion
            );
        }
        return dockerfileScript;
    }

    private DockerfileTemplate createBackendDockerfile(String buildTool) {
        if (buildTool.equalsIgnoreCase("maven")) {
            return new BackendDockerfile(new MavenBuildToolStrategy());
        } else if (buildTool.equalsIgnoreCase("gradle")) {
            return new BackendDockerfile(new GradleBuildToolStrategy());
        }
        throw new IllegalArgumentException("Unsupported backend build tool: " + buildTool);
    }

    private DockerfileTemplate createFrontendDockerfile(String buildTool, String packageManager) {
        PackageManagerStrategy packageManagerStrategy;
        BuildToolStrategy buildToolStrategy;

        if (packageManager.equalsIgnoreCase("yarn")) {
            packageManagerStrategy = new YarnStrategy();
        } else if (packageManager.equalsIgnoreCase("npm")) {
            packageManagerStrategy = new NpmStrategy();
        } else {
            throw new IllegalArgumentException("Unsupported frontend package manager: " + packageManager);
        }

        if (buildTool.equalsIgnoreCase("cra")) {
            buildToolStrategy = new CraStrategy();
        } else if (buildTool.equalsIgnoreCase("vite")) {
            buildToolStrategy = new ViteStrategy();
        } else {
            throw new IllegalArgumentException("Unsupported frontend build tool: " + buildTool);
        }

        return new FrontendDockerfile(packageManagerStrategy, buildToolStrategy);
    }
}
