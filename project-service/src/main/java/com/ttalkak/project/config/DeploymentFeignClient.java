package com.ttalkak.project.config;

import com.ttalkak.project.project.framework.deploymentadapter.dto.DeploymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "DEPLOYMENT-SERVICE")
public interface DeploymentFeignClient {
    
    // feign 예시
//    @DeleteMapping("/feign/deployment/{projectId}")
//    void deleteDeployment(@PathVariable("projectId")Long projectId);

    @GetMapping("/feign/deployment/project/{projectId}")
    List<DeploymentResponse> getDeployments(@PathVariable("projectId")Long projectId);

}
