package com.ttalkak.deployment.deployment.domain.model.docker.backend;

public class GradleBuildToolStrategy implements BuildToolStrategy {

    @Override
    public String setupBaseImage(String languageVersion) {
        return "FROM gradle:8.8-jdk" + languageVersion + " AS build\n";
    }

    @Override
    public String runBuildCommand() {
        return "RUN gradle clean build -x test --no-daemon\n";
    }

    @Override
    public String copyArtifact() {
        return "COPY --from=build /app/build/libs/*.jar /app/app.jar\n";
    }
}
