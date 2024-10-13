package com.ttalkak.deployment.deployment.domain.model.docker.frontend.packagemanager;

public class YarnStrategy implements PackageManagerStrategy {

    @Override
    public String copyDependencies() {
        return "COPY package.json yarn.lock ./\n";
    }

    @Override
    public String installDependencies() {
        return "RUN yarn install --frozen-lockfile\n";
    }

    @Override
    public String runBuild() {
        return "RUN yarn build\n";
    }
}
