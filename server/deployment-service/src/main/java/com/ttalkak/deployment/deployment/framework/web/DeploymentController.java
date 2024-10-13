package com.ttalkak.deployment.deployment.framework.web;

import com.ttalkak.deployment.common.ApiResponse;
import com.ttalkak.deployment.deployment.application.usecase.*;
import com.ttalkak.deployment.deployment.framework.web.request.*;
import com.ttalkak.deployment.deployment.framework.web.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/deployment")
public class DeploymentController {

    private final CreateDeploymentUseCase createDeploymentUsecase;

    private final CreateDockerFileUseCaseRegacy createDockerFileUsecaseRegacy;

    private final UpdateDeploymentUseCase updateDeploymentUsecase;

    private final DeleteDeploymentUseCase deleteDeploymentUsecase;

    private final CommandDeploymentStatusUseCase commandDeploymentStatusUsecase;

    private final inquiryUseCase inquiryUsecase;

    private final CreateDatabaseUseCase createDatabaseUsecase;

    private final CommandDatabaseStatusUseCase commandDatabaseStatusUseCase;

    private final DeleteDatabaseUseCase deleteDatabaseUsecase;

    // 배포 등록
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DeploymentCreateResponse> createDeployment(@RequestBody DeploymentCreateRequest deploymentCreateRequest){
        DeploymentCreateResponse deployment = createDeploymentUsecase.createDeployment(deploymentCreateRequest);
        return ApiResponse.created(deployment);
    }

    // 배포 검색조회
    @GetMapping
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
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<DeploymentDetailResponse> updateDeployment(@RequestHeader("X-USER-ID") Long userId, @RequestBody DeploymentUpdateRequest deploymentUpdateRequest){
        DeploymentDetailResponse deployment = updateDeploymentUsecase.updateDeployment(userId, deploymentUpdateRequest);
        return ApiResponse.success(deployment);
    }

    // 도커 파일 생성
    @PostMapping("/dockerfile")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> createDockerFile(@RequestHeader("X-USER-ID") Long userId, @RequestBody DockerfileCreateRequestRegacy dockerfileCreateRequestRegacy){
        createDockerFileUsecaseRegacy.createDockerFile(userId, dockerfileCreateRequestRegacy);
        return ApiResponse.success();
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

    // 배포 삭제
    @DeleteMapping("/{deploymentId}")
    public ApiResponse<Void> deleteDeployment(@RequestHeader("X-USER-ID") Long userId, @PathVariable("deploymentId") Long deploymentId){
        deleteDeploymentUsecase.deleteDeployment(userId, deploymentId);
        return ApiResponse.empty();
    }

    // 데이터베이스 생성
    @PostMapping("/database")
    public ApiResponse<DatabasePreviewResponse> createDatabase(@RequestHeader("X-USER-ID") Long userId, @RequestBody DatabaseCreateRequest databaseCreateRequest){
        DatabasePreviewResponse database = createDatabaseUsecase.createDatabase(userId, databaseCreateRequest);
        return ApiResponse.created(database);
    }

    // 데이터베이스 단건조회
    @GetMapping("/database/{databaseId}")
    public ApiResponse<DatabaseDetailResponse> getDatabase(@PathVariable("databaseId") Long databaseId){
        DatabaseDetailResponse database = inquiryUsecase.getDatabase(databaseId);
        return ApiResponse.success(database);
    }

    // 데이터베이스 삭제
    @DeleteMapping("/database/{databaseId}")
    public ApiResponse<Void> deleteDatabase(@RequestHeader("X-USER-ID") Long userId, @PathVariable("databaseId") Long databaseId){
        deleteDatabaseUsecase.deleteDatabase(userId, databaseId);
        return ApiResponse.empty();
    }

    //데이터베이스 상태 변경
    @PostMapping("/database/command")
    public ApiResponse<Void> commandDatabase(@RequestHeader("X-USER-ID") Long userId, @RequestBody DatabaseCommandStatusRequest databaseCommandStatusRequest){
        commandDatabaseStatusUseCase.commandDatabaseStatus(userId, databaseCommandStatusRequest);
        return ApiResponse.empty();
    }

    /**
     * 프로젝트 페이징 조회
     * @param page
     * @param size
     * @param sort
     * @param direction
     * @param searchKeyword
     * @param userId
     * @return
     */
    @GetMapping("/database/search")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<DatabasePageResponse> getProjectsByPageable(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "9") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sort,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(required = false) String searchKeyword,
            @RequestHeader("X-USER-ID") Long userId) {
        if (searchKeyword == null) searchKeyword = "";
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        DatabasePageResponse pages = inquiryUsecase.getDatabases(pageable, searchKeyword, userId);
        return ApiResponse.success(pages);
    }
}