package com.ttalkak.deployment.deployment.framework.web;

import com.ttalkak.deployment.common.WebAdapter;
import com.ttalkak.deployment.deployment.application.usecase.WebHookCommand;
import com.ttalkak.deployment.deployment.application.usecase.WebHookDeploymentUsecase;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import com.ttalkak.deployment.deployment.framework.web.request.DeploymentWebHookRequest;
import com.ttalkak.deployment.deployment.framework.web.request.WebHookCommit;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/webhook/deployment")
public class DeploymentWebHookController {
    private final WebHookDeploymentUsecase webHookDeploymentUsecase;

    @PostMapping("/backend/{webhookToken}")
    public void deploymentWebHook(
            @PathVariable String webhookToken,
            @RequestBody DeploymentWebHookRequest deploymentWebHookRequest
    ){
        WebHookCommand command = new WebHookCommand(
                deploymentWebHookRequest.getRepository().getName(),
                deploymentWebHookRequest.getRepository().getUrl(),
                deploymentWebHookRequest.getSender().getLogin(),
                deploymentWebHookRequest.getSender().getAvatarUrl(),
                deploymentWebHookRequest.getCommits().stream().findFirst().orElse(new WebHookCommit("")).getMessage()
        );
        webHookDeploymentUsecase.createDeploymentWebHook(ServiceType.BACKEND, webhookToken, command);
    }

    @PostMapping("/frontend/{webhookToken}")
    public void deploymentWebHookFrontend(
            @PathVariable String webhookToken,
            @RequestBody DeploymentWebHookRequest deploymentWebHookRequest
    ){
        WebHookCommand command = new WebHookCommand(
                deploymentWebHookRequest.getRepository().getName(),
                deploymentWebHookRequest.getRepository().getUrl(),
                deploymentWebHookRequest.getSender().getLogin(),
                deploymentWebHookRequest.getSender().getAvatarUrl(),
                deploymentWebHookRequest.getCommits().stream().findFirst().orElse(new WebHookCommit("")).getMessage()
        );

        webHookDeploymentUsecase.createDeploymentWebHook(ServiceType.FRONTEND, webhookToken, command);
    }
}
