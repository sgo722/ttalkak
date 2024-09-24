package com.ttalkak.deployment.deployment.framework.web.request;

import com.ttalkak.deployment.deployment.framework.jpaadapter.repository.DeploymentRepository;
import lombok.Data;

import java.util.List;

@Data
public class DeploymentWebHookRequest {
    private WebHookRepository repository;
    private WebHookUser sender;
    private List<WebHookCommit> commits;
}

