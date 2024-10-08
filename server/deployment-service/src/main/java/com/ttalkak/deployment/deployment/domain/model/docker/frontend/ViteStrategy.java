package com.ttalkak.deployment.deployment.domain.model.docker.frontend;

public class ViteStrategy implements BuildToolStrategy {

    @Override
    public String copyBuildOutput() {
        return "COPY --from=build /app/dist /usr/share/nginx/html\n";
    }
}
