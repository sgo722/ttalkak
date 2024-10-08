package com.ttalkak.deployment.deployment.domain.model.docker.frontend;

import com.ttalkak.deployment.deployment.domain.model.docker.DockerfileTemplate;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;

public class FrontendDockerfile extends DockerfileTemplate {

    private final PackageManagerStrategy packageManagerStrategy;
    private final BuildToolStrategy buildToolStrategy;

    public FrontendDockerfile(PackageManagerStrategy packageManagerStrategy, BuildToolStrategy buildToolStrategy) {
        this.packageManagerStrategy = packageManagerStrategy;
        this.buildToolStrategy = buildToolStrategy;
    }

    @Override
    protected String setupBaseImage(String buildTool, String languageVersion) {
        return "FROM node:" + languageVersion + " AS build\n";
    }

    @Override
    protected String runBuildCommand(String buildTool, String packageManager) {
        StringBuilder command = new StringBuilder();
        command.append(packageManagerStrategy.copyDependencies())
                .append(packageManagerStrategy.installDependencies())
                .append("COPY . .\n")
                .append(packageManagerStrategy.runBuild());
        return command.toString();
    }

    @Override
    protected String setupFinalStage(ServiceType serviceType, String buildTool, String languageVersion) {
        return "FROM nginx:stable-alpine\n" +
                buildToolStrategy.copyBuildOutput() +
                "RUN chmod -R 755 /usr/share/nginx/html\n" +
                "CMD [\"nginx\", \"-g\", \"daemon off;\"]";
    }
}
