package com.ttalkak.deployment.deployment.framework.web;

import com.ttalkak.deployment.common.ApiResponse;
import com.ttalkak.deployment.deployment.application.usecase.inquiryUseCase;
import com.ttalkak.deployment.deployment.application.usecase.UpdateDeploymentStatusUseCase;
import com.ttalkak.deployment.deployment.framework.web.request.DeploymentUpdateStatusRequest;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentPreviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feign")
public class DeploymentFeignController {

    private final inquiryUseCase inquiryUsecase;

    private final UpdateDeploymentStatusUseCase updateDeploymentStatusUsecase;

    // 하나의 프로젝트에 포함되는 배포이력 전체조회
    @GetMapping("/deployment/project/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public List<DeploymentPreviewResponse> getAllDeploymentByProjectId(@PathVariable("projectId") Long projectId){
        return inquiryUsecase.getDeploymentsByProjectId(projectId);
    }

    @PostMapping("/deployment/status")
    public ApiResponse<Void> updateDeploymentStatus(@RequestBody DeploymentUpdateStatusRequest deploymentUpdateStatusRequest){
        updateDeploymentStatusUsecase.updateDeploymentStatus(deploymentUpdateStatusRequest);
        return ApiResponse.success(null);
    }
}
