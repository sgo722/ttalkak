package com.ttalkak.deployment.deployment.domain.model.docker.frontend.packagemanager;

public interface PackageManagerStrategy {
    String copyDependencies();
    String installDependencies();
    String runBuild();
}
