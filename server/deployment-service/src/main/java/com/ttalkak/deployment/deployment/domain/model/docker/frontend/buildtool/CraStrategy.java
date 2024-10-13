package com.ttalkak.deployment.deployment.domain.model.docker.frontend.buildtool;

public class CraStrategy implements BuildToolStrategy {

    @Override
    public String buildFromImage(String languageVersion) {
        return "FROM nginx:stable-alpine\n";
    }

    @Override
    public String copyBuildOutput(){
        return "COPY --from=build /app/build /usr/share/nginx/html\n" +
                "RUN chmod -R 755 /usr/share/nginx/html\n";
    }

    @Override
    public String cmdCommand() {
        return "CMD [\"nginx\", \"-g\", \"daemon off;\"]";
    }
}
