package com.ttalkak.deployment.deployment.domain.model.docker.frontend.buildtool;

public interface BuildToolStrategy {

    String buildFromImage(String languageVersion);

    String copyBuildOutput();

    String cmdCommand();
}

