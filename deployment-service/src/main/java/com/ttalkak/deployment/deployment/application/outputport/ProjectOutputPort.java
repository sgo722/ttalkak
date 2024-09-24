package com.ttalkak.deployment.deployment.application.outputport;


import com.ttalkak.deployment.deployment.framework.projectadapter.dto.ProjectInfoResponse;
import com.ttalkak.deployment.deployment.framework.projectadapter.dto.ProjectWebHookResponse;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


@Service
public interface ProjectOutputPort {
    ProjectInfoResponse getProjectInfo(Long projectId);
    ProjectWebHookResponse getWebHookProject(String webhookToken);
}
