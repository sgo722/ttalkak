package com.ttalkak.deployment.deployment.application.inputport;

import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.usecase.CreateDockerfileUseCase;
import com.ttalkak.deployment.deployment.domain.model.docker.*;
import com.ttalkak.deployment.deployment.domain.model.docker.backend.BackendDockerfile;
import com.ttalkak.deployment.deployment.domain.model.docker.backend.GradleBuildToolStrategy;
import com.ttalkak.deployment.deployment.domain.model.docker.backend.MavenBuildToolStrategy;
import com.ttalkak.deployment.deployment.domain.model.docker.frontend.*;
import com.ttalkak.deployment.deployment.domain.model.docker.frontend.buildtool.BuildToolStrategy;
import com.ttalkak.deployment.deployment.domain.model.docker.frontend.buildtool.CraStrategy;
import com.ttalkak.deployment.deployment.domain.model.docker.frontend.buildtool.NextBuildToolStrategy;
import com.ttalkak.deployment.deployment.domain.model.docker.frontend.buildtool.ViteStrategy;
import com.ttalkak.deployment.deployment.domain.model.docker.frontend.packagemanager.NextPackageManagerStrategy;
import com.ttalkak.deployment.deployment.domain.model.docker.frontend.packagemanager.NpmStrategy;
import com.ttalkak.deployment.deployment.domain.model.docker.frontend.packagemanager.PackageManagerStrategy;
import com.ttalkak.deployment.deployment.domain.model.docker.frontend.packagemanager.YarnStrategy;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;

@UseCase
public class CreateDockerFileInputPort implements CreateDockerfileUseCase {

    @Override
    public String generateDockerfile(String framework, ServiceType serviceType, String buildTool, String packageManager, String languageVersion) {
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
            DockerfileTemplate frontendDockerfile = createFrontendDockerfile(framework, buildTool, packageManager);
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
        if (buildTool != null && buildTool.equalsIgnoreCase("maven")) {
            return new BackendDockerfile(new MavenBuildToolStrategy());
        } else if (buildTool != null && buildTool.equalsIgnoreCase("gradle")) {
            return new BackendDockerfile(new GradleBuildToolStrategy());
        }
        throw new BusinessException(ErrorCode.NOT_DETECTED_GIT_REPOSITORY);
    }

    private DockerfileTemplate createFrontendDockerfile(String framework, String buildTool, String packageManager) {
        PackageManagerStrategy packageManagerStrategy;
        BuildToolStrategy buildToolStrategy;

        if(framework.equals("NEXTJS")){
            packageManagerStrategy = new NextPackageManagerStrategy();
            buildToolStrategy = new NextBuildToolStrategy();
        }

        else {
            if (packageManager != null && packageManager.equalsIgnoreCase("yarn")) {
                packageManagerStrategy = new YarnStrategy();
            } else if (packageManager != null && packageManager.equalsIgnoreCase("npm")) {
                packageManagerStrategy = new NpmStrategy();
            } else {
                throw new BusinessException(ErrorCode.NOT_DETECTED_GIT_REPOSITORY);
            }

            if (buildTool != null && buildTool.equalsIgnoreCase("cra")) {
                buildToolStrategy = new CraStrategy();
            } else if (buildTool != null && buildTool.equalsIgnoreCase("vite")) {
                buildToolStrategy = new ViteStrategy();
            } else {
                throw new BusinessException(ErrorCode.NOT_DETECTED_GIT_REPOSITORY);
            }
        }

        return new FrontendDockerfile(packageManagerStrategy, buildToolStrategy);
    }
}
