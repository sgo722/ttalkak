package com.ttalkak.deployment.deployment.domain.model.docker.frontend;

public interface PackageManagerStrategy {
    String copyDependencies();
    String installDependencies();
    String runBuild();
}
