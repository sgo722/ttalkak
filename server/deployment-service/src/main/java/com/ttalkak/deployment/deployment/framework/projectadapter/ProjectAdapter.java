package com.ttalkak.deployment.deployment.framework.projectadapter;

import com.ttalkak.deployment.config.ProjectFeignClient;
import com.ttalkak.deployment.deployment.application.outputport.ProjectOutputPort;
import com.ttalkak.deployment.deployment.framework.projectadapter.dto.ProjectInfoResponse;
import com.ttalkak.deployment.deployment.framework.projectadapter.dto.ProjectWebHookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectAdapter implements ProjectOutputPort {

    private final ProjectFeignClient projectFeignClient;

    @Override
    public ProjectInfoResponse getProjectInfo(Long projectId) {
        return projectFeignClient.getProjectInfo(projectId);
    }

    @Override
    public ProjectWebHookResponse getWebHookProject(String webhookToken) {
        return projectFeignClient.getWebHookProject(webhookToken);
    }
}
