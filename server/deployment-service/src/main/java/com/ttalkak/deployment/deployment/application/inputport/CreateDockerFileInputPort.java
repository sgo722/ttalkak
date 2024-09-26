package com.ttalkak.deployment.deployment.application.inputport;


import com.ttalkak.deployment.deployment.application.usecase.CreateDockerfileUseCase;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateDockerFileInputPort implements CreateDockerfileUseCase {

    @Override
    public String generateDockerfileScript(ServiceType serviceType, String buildTool, String packageManager, String languageVersion) {
        StringBuilder dockerfileBuilder = new StringBuilder();

        if(serviceType == ServiceType.BACKEND) {
            if("gradle".equalsIgnoreCase(buildTool)) {
                dockerfileBuilder.append("FROM gradle:8.8-jdk").append(languageVersion).append(" AS build\n");
            } else if("maven".equalsIgnoreCase(buildTool)) {
                dockerfileBuilder.append("FROM maven:3.9.4-eclipse-temurin-").append(languageVersion).append(" AS build\n");
            }

            dockerfileBuilder.append("WORKDIR /app\n")
                    .append("COPY . .\n");

            if("gradle".equalsIgnoreCase(buildTool)) {
                dockerfileBuilder.append("RUN gradle clean build -x test --no-daemon\n");
            } else if("maven".equalsIgnoreCase(buildTool)) {
                dockerfileBuilder.append("RUN mvn clean package -DskipTests\n");
            }

            dockerfileBuilder.append("FROM eclipse-temurin:").append(languageVersion).append("-jdk\n")
                    .append("WORKDIR /app\n");


            if("gradle".equalsIgnoreCase(buildTool)) {
                dockerfileBuilder.append("COPY --from=build /app/build/libs/*.jar /app/app.jar\n");
            } else if("maven".equalsIgnoreCase(buildTool)) {
                dockerfileBuilder.append("COPY --from=build /app/target/*.jar /app/app.jar\n");
            }

            dockerfileBuilder.append("ENTRYPOINT [\"java\", \"-jar\", \"/app/app.jar\"]\n");
        }

        if(serviceType == ServiceType.FRONTEND) {
            dockerfileBuilder.append("FROM node:").append(languageVersion).append(" AS build\n")
                    .append("WORKDIR /app\n");

            if("npm".equalsIgnoreCase(packageManager)) {
                dockerfileBuilder.append("COPY package*.json ./\n")
                        .append("RUN npm ci\n")
                        .append("COPY . .\n")
                        .append("RUN ").append(packageManager).append("run build\n");
            } else if("yarn".equalsIgnoreCase(packageManager)) {
                dockerfileBuilder.append("COPY package.json yarn.lock ./\n")
                        .append("RUN yarn install --frozen-lockfile\n")
                        .append("COPY . .\n")
                        .append("RUN ").append("yarn build\n");
            }

            dockerfileBuilder.append("FROM nginx:stable-alpine\n");

            if("CRA".equalsIgnoreCase(buildTool)) {
                dockerfileBuilder.append("COPY --from=build /app/build /usr/share/nginx/html\n");
            } else if("Vite".equalsIgnoreCase(buildTool)) {
                dockerfileBuilder.append("COPY --from=build /app/dist /usr/share/nginx/html\n");
            }

            dockerfileBuilder.append("CMD [\"nginx\", \"-g\", \"daemon off;\"]");
        }

        return dockerfileBuilder.toString();
    }

}
