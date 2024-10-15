package com.ttalkak.deployment.deployment.domain.model.docker.frontend;

public class CraStrategy implements BuildToolStrategy{

    @Override
    public String copyBuildOutput(){
        return "COPY --from=build /app/build /usr/share/nginx/html\n";
    }
}
