package com.ttalkak.deployment.deployment.framework.web.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DockerfileCreateRequest {
    // 도커 파일 존재 여부
    private boolean exist;

    // 빌드도구
    private String buildTool;

    // 프론트엔드
    private String packageManager;

    // 프론트엔드, 백엔드 (node or java version)
    private String languageVersion;
}
