package com.ttalkak.deployment.deployment.framework.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class DockerFileResponse {
    private String dockerfileCode;

    @Builder
    private DockerFileResponse(String dockerfileCode) {
        this.dockerfileCode = dockerfileCode;
    }

    public static DockerFileResponse of(String dockerfileCode) {
        return DockerFileResponse.builder()
                .dockerfileCode(dockerfileCode)
                .build();
    }
}
