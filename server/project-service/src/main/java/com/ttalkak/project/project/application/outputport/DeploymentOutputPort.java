package com.ttalkak.project.project.application.outputport;


import com.ttalkak.project.project.framework.deploymentadapter.dto.DeploymentResponse;
import java.util.List;

public interface DeploymentOutputPort {

    // feign 예시
    //public void deleteDeployment(Long projectId);

    public List<DeploymentResponse> getDeployments(Long projectId);
}
