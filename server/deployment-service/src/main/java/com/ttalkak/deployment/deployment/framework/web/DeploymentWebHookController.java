package com.ttalkak.deployment.deployment.framework.web;

import com.ttalkak.deployment.common.WebAdapter;
import com.ttalkak.deployment.deployment.application.usecase.WebHookCommand;
import com.ttalkak.deployment.deployment.application.usecase.WebHookDeploymentUseCase;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import com.ttalkak.deployment.deployment.framework.web.request.DeploymentWebHookRequest;
import com.ttalkak.deployment.deployment.framework.web.request.WebHookCommit;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@WebAdapter
@RequiredArgsConstructor
@RestController
@RequestMapping("/webhook/deployment")
public class DeploymentWebHookController {
    private final WebHookDeploymentUseCase webHookDeploymentUsecase;

    @PostMapping("/backend/{webhookToken}")
    public void deploymentWebHook(
            @PathVariable("webhookToken") String webhookToken,
            @RequestBody DeploymentWebHookRequest deploymentWebHookRequest
    ){
        WebHookCommand command = new WebHookCommand(
                deploymentWebHookRequest.getRepository().getName(),
                deploymentWebHookRequest.getRepository().getUrl(),
                deploymentWebHookRequest.getSender().getLogin(),
                deploymentWebHookRequest.getSender().getAvatarUrl(),
                Optional.ofNullable(deploymentWebHookRequest.getCommits())
                        .orElse(Collections.emptyList())
                        .stream()
                        .findFirst()
                        .orElse(new WebHookCommit(""))
                        .getMessage()
        );
        webHookDeploymentUsecase.createDeploymentWebHook(ServiceType.BACKEND, webhookToken, command);
    }

    @PostMapping("/frontend/{webhookToken}")
    public void deploymentWebHookFrontend(
            @PathVariable("webhookToken") String webhookToken,
            @RequestBody DeploymentWebHookRequest deploymentWebHookRequest
    ){
        WebHookCommand command = new WebHookCommand(
                deploymentWebHookRequest.getRepository().getName(),
                deploymentWebHookRequest.getRepository().getUrl(),
                deploymentWebHookRequest.getSender().getLogin(),
                deploymentWebHookRequest.getSender().getAvatarUrl(),
                Optional.ofNullable(deploymentWebHookRequest.getCommits())
                        .orElse(Collections.emptyList())
                        .stream()
                        .findFirst()
                        .orElse(new WebHookCommit(""))
                        .getMessage()
        );

        webHookDeploymentUsecase.createDeploymentWebHook(ServiceType.FRONTEND, webhookToken, command);
    }
}
