package com.ttalkak.deployment.deployment.domain.model.docker.frontend.buildtool;

public class NextBuildToolStrategy implements BuildToolStrategy {

    @Override
    public String buildFromImage(String languageVersion) {
        return "FROM node:"+languageVersion + "\n" +
                "WORKDIR /app\n";
    }

    @Override
    public String copyBuildOutput() {
        return "COPY --from=build /app/package*.json ./\n" +
                "COPY --from=build /app/.next ./.next\n" +
                "COPY --from=build /app/public ./public\n" +
                "COPY --from=build /app/node_modules ./node_modules\n";
    }

    @Override
    public String cmdCommand() {
        return "CMD [\"npx\", \"next\", \"start\"]";
    }
}
