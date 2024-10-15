package com.ttalkak.deployment.deployment.domain.model.docker.frontend;

public class NpmStrategy implements PackageManagerStrategy {

    @Override
    public String copyDependencies() {
        return "COPY package*.json ./\n";
    }

    @Override
    public String installDependencies() {
        return "RUN npm i\n";
    }

    @Override
    public String runBuild() {
        return "RUN npm run build\n";
    }
}
