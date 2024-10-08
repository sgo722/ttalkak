package com.ttalkak.deployment.deployment.domain.model.docker.backend;

public class MavenBuildToolStrategy implements BuildToolStrategy {

    @Override
    public String setupBaseImage(String languageVersion) {
        return "FROM maven:3.9.4-eclipse-temurin-" + languageVersion + " AS build\n";
    }

    @Override
    public String runBuildCommand() {
        return "RUN mvn clean package -DskipTests\n";
    }

    @Override
    public String copyArtifact() {
        return "COPY --from=build /app/target/*.jar /app/app.jar\n";
    }
}
