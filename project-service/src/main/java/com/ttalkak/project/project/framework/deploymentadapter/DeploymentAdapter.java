package com.ttalkak.project.project.framework.deploymentadapter;

import com.ttalkak.project.config.DeploymentFeignClient;
import com.ttalkak.project.project.application.outputport.DeploymentOutputPort;
import com.ttalkak.project.project.framework.deploymentadapter.dto.DeploymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeploymentAdapter implements DeploymentOutputPort {

    private final DeploymentFeignClient deploymentFeignClient;

    // feign 예시
//    @Override
//    public void deleteDeployment(Long projectId) {
//        deploymentFeignClient.deleteDeployment(projectId);
//    }

    @Override
    public List<DeploymentResponse> getDeployments(Long projectId) {
        return deploymentFeignClient.getDeployments(projectId);
    }
}
