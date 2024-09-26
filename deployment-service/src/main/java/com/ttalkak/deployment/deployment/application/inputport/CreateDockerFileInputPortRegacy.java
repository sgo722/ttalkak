package com.ttalkak.deployment.deployment.application.inputport;

import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.outputport.GithubOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.UserOutputPort;
import com.ttalkak.deployment.deployment.framework.useradapter.dto.UserInfoResponse;
import com.ttalkak.deployment.deployment.application.usecase.CreateDockerFileUsecaseRegacy;
import com.ttalkak.deployment.deployment.framework.web.request.DockerfileCreateRequestRegacy;
import com.ttalkak.deployment.deployment.framework.web.response.DockerFileResponse;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateDockerFileInputPortRegacy implements CreateDockerFileUsecaseRegacy {

    private static final Logger log = LoggerFactory.getLogger(CreateDockerFileInputPortRegacy.class);

    private final UserOutputPort userOutputPort;
    private final ChatgptService chatgptService;
    private final GithubOutputPort githubOutputPort;

    @Override
    public DockerFileResponse createDockerFile(Long userId, DockerfileCreateRequestRegacy dockerfileCreateRequestRegacy) {

        String buildEnv = dockerfileCreateRequestRegacy.getBuildEnv();
        String gitTree = dockerfileCreateRequestRegacy.getGitTree();
        String prompt = buildEnv + "\n" + gitTree + "\n 패키지 구조와 프로젝트 설정 정보야. \n 도커파일 만들어줘. 딱 코드만 알려줘";

        DockerFileResponse dockerFileResponse;
        try {
            dockerFileResponse = DockerFileResponse.of(chatgptService.sendMessage(prompt));
        } catch (Exception e) {
            log.error("Failed to generate Dockerfile via ChatGPT", e);
            throw new BusinessException(ErrorCode.GPT_NOT_CREATE_DOCKERFILE);
        }
        String dockerfileCode = dockerFileResponse.getDockerfileCode();
        String encodedDockerfileCode = Base64.getEncoder().encodeToString(dockerfileCode.getBytes(StandardCharsets.UTF_8));

        UserInfoResponse userInfo = userOutputPort.getUserInfo(userId);
        String accessToken = "token " + userInfo.getAccessToken();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("message", "Add Dockerfile by ttalkak");
        requestBody.put("content", encodedDockerfileCode);
        requestBody.put("branch", dockerfileCreateRequestRegacy.getBranch());

        try {
            Map<String, Object> dockerFile = githubOutputPort.createDockerFile(
                    accessToken,
                    dockerfileCreateRequestRegacy.getOwner(),
                    dockerfileCreateRequestRegacy.getRepo(),
                    dockerfileCreateRequestRegacy.getRootDirectory(),
                    requestBody
            );

            log.info("dockerFile :: " + dockerFile.toString());
        } catch (Exception e) {
            log.error("Failed to upload Dockerfile to GitHub", e);
            throw new BusinessException(ErrorCode.GITHUB_FEIGN_ERROR);
        }

        log.info("dockerFileResponse: {}", dockerFileResponse);
        return dockerFileResponse;
    }
}
