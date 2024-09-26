package com.ttalkak.deployment.deployment.framework.web;

import com.ttalkak.deployment.common.ApiResponse;
import com.ttalkak.deployment.deployment.application.usecase.*;
import com.ttalkak.deployment.deployment.framework.web.request.*;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentCreateResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentDetailResponse;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentPreviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/deployment")
public class DeploymentController {

    private final CreateDeploymentUsecase createDeploymentUsecase;

    private final CreateDockerFileUsecaseRegacy createDockerFileUsecaseRegacy;

    private final UpdateDeploymentUsecase updateDeploymentUsecase;

    private final DeleteDeploymentUsecase deleteDeploymentUsecase;

    private final CommandDeploymentStatusUsecase commandDeploymentStatusUsecase;

    private final InquiryUsecase inquiryUsecase;

    // 도커 파일 생성
    @PostMapping("/dockerfile")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> createDockerFile(@RequestHeader("X-USER-ID") Long userId, @RequestBody DockerfileCreateRequestRegacy dockerfileCreateRequestRegacy){
        createDockerFileUsecaseRegacy.createDockerFile(userId, dockerfileCreateRequestRegacy);
        return ApiResponse.success();
    }

    // 배포 등록
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DeploymentCreateResponse> createDeployment(@RequestBody DeploymentCreateRequest deploymentCreateRequest){
        DeploymentCreateResponse deployment = createDeploymentUsecase.createDeployment(deploymentCreateRequest);
        return ApiResponse.created(deployment);
    }

    // 배포 상태 변경
    @PostMapping("/command")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Void> updateDeploymentStatus(@RequestBody DeploymentCommandStatusRequest deploymentCommandStatusRequest){
        commandDeploymentStatusUsecase.commandDeploymentStatus(deploymentCommandStatusRequest);
        return ApiResponse.empty();
    }

    // 배포 상세조회
    @GetMapping("/{deploymentId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<DeploymentDetailResponse> getDeployment(@PathVariable("deploymentId") Long deploymentId){
        DeploymentDetailResponse deployment = inquiryUsecase.getDeployment(deploymentId);
        return ApiResponse.success(deployment);
    }

    // 배포 검색조회
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<DeploymentPreviewResponse>> searchDeploymentByGithubRepositoryName(
            @RequestParam(value = "githubRepoName") String githubRepoName,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ){
        List<DeploymentPreviewResponse> deployments = inquiryUsecase.searchDeploymentByGithubRepositoryName(githubRepoName, page, size);
        return ApiResponse.success(deployments);
    }

    // 배포 수정
    @PatchMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<DeploymentDetailResponse> updateDeployment(@RequestHeader("X-USER-ID") Long userId, @RequestBody DeploymentUpdateRequest deploymentUpdateRequest){
        DeploymentDetailResponse deployment = updateDeploymentUsecase.updateDeployment(userId, deploymentUpdateRequest);
        return ApiResponse.success(deployment);
    }

    // 배포 삭제
    @DeleteMapping("/{deploymentId}")
    public ApiResponse<Void> deleteDeployment(@RequestHeader("X-USER-ID") Long userId, @PathVariable("deploymentId") Long deploymentId){
        deleteDeploymentUsecase.deleteDeployment(userId, deploymentId);
        return ApiResponse.empty();
    }
}