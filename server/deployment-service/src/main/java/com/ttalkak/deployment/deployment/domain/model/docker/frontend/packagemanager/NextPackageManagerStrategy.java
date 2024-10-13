package com.ttalkak.deployment.deployment.domain.model.docker.frontend.packagemanager;

public class NextPackageManagerStrategy implements PackageManagerStrategy {

    @Override
    public String copyDependencies() {
        return "COPY package*.json ./\n";
    }

    @Override
    public String installDependencies() {
        return "RUN npm install --frozen-lockfile\n" +
                "COPY . .\n";
    }

    @Override
    public String runBuild() {
        return "RUN npm run build\n";
    }
}
