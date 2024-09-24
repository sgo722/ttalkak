package com.ttalkak.deployment.deployment.application.inputport;

import com.ttalkak.deployment.deployment.framework.web.CreateDockerFileUsecase;
import com.ttalkak.deployment.deployment.framework.web.request.DockerfileCreateRequest;
import com.ttalkak.deployment.deployment.framework.web.response.DockerFileResponse;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateDockerFileInputPort implements CreateDockerFileUsecase {

    private final ChatgptService chatgptService;

    @Override
    public DockerFileResponse createDockerFile(DockerfileCreateRequest dockerfileCreateRequest) {

        String buildEnv = dockerfileCreateRequest.getBuildEnv();
        String gitTree = dockerfileCreateRequest.getGitTree();
        String prompt = buildEnv + "\n" + gitTree + "\n 패키지 구조와 프로젝트 설정 정보야. \n 도커파일 만들어줘. 딱 코드만 알려줘";
        return DockerFileResponse.of(chatgptService.sendMessage(prompt));
    }
}
