package com.ttalkak.project.project.framework.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.project.common.ApiResponse;
import com.ttalkak.project.common.WebAdapter;
import com.ttalkak.project.project.application.usecase.CreateProjectUseCase;
import com.ttalkak.project.project.application.usecase.DeleteProjectUseCase;
import com.ttalkak.project.project.application.usecase.GetProjectUseCase;
import com.ttalkak.project.project.application.usecase.UpdateProjectUseCase;
import com.ttalkak.project.project.framework.web.request.DomainNameRequest;
import com.ttalkak.project.project.framework.web.request.ProjectCreateRequest;
import com.ttalkak.project.project.framework.web.request.ProjectUpdateRequest;
import com.ttalkak.project.project.framework.web.response.DomainNameResponse;
import com.ttalkak.project.project.framework.web.response.ProjectCreateResponse;
import com.ttalkak.project.project.framework.web.response.ProjectPageResponse;
import com.ttalkak.project.project.framework.web.response.ProjectDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ehcache.core.Ehcache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/v1")
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;

    private final GetProjectUseCase getProjectUseCase;

    private final UpdateProjectUseCase updateProjectUseCase;

    private final DeleteProjectUseCase deleteProjectUseCase;

    private final CacheManager cacheManager;

    /**
     * 프로젝트 생성
     * @param projectCreateRequest
     * @return
     */
    @PostMapping("/project")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProjectCreateResponse> createProject(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody ProjectCreateRequest projectCreateRequest) {
        return new ApiResponse<>(true, "", 201, createProjectUseCase.createProject(userId, projectCreateRequest));
    }

    /**
     * 프로젝트 단건 조회
     * @param projectId
     * @return
     */
    @GetMapping("/project/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProjectDetailResponse> getProject(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long projectId) {
        ProjectDetailResponse projectDetailResponse = getProjectUseCase.getProject(userId, projectId);
        return ApiResponse.success(projectDetailResponse);
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
    @GetMapping("/project/search")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProjectPageResponse> getProjectsByPageable(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "9") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sort,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction,
            @RequestParam String searchKeyword,
            @RequestHeader("X-USER-ID") Long userId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        ProjectPageResponse pages = getProjectUseCase.getProjects(pageable, searchKeyword, userId);
        return ApiResponse.success(pages);
    }

    /**
     * 프로젝트 수정
     * @param userId
     * @return
     */
    @PatchMapping("/project/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ProjectDetailResponse> updateProject(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long projectId,
            @RequestBody ProjectUpdateRequest projectUpdateRequest) {
        return ApiResponse.success(updateProjectUseCase.updateProject(userId, projectId, projectUpdateRequest));
    }

    /**
     * 프로젝트 삭제
     * @param projectId
     * @return
     */
    @DeleteMapping("/project/{projectId}")
    public ApiResponse<Void> deleteProject(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long projectId) throws JsonProcessingException {
        deleteProjectUseCase.deleteProject(userId, projectId);
        return ApiResponse.success();
    }
    
    /**
     * 도메인명 중복 체크
     * @param request
     * @return
     */
    @PostMapping("/project/domain/check")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<DomainNameResponse> isDuplicateDomainName(@RequestBody DomainNameRequest request) {
        DomainNameResponse.DomainNameResponseBuilder responseBuilder = DomainNameResponse.builder();
        if(!getProjectUseCase.isDuplicateDomainName(request)) {
            responseBuilder.canMake(true).message("생성할 수 있는 도메인입니다.");
        } else {
            responseBuilder.canMake(false).message("이미 존재하는 도메인입니다.");
        }

        return ApiResponse.success(responseBuilder.build());

    }

    @GetMapping("/project/ehcache")
    public List<Map<String, Object>> findAll() {
        return cacheManager.getCacheNames().stream()
                  .map(cacheName -> {
                    Map<String, Object> cacheData = new HashMap<>();
                    cacheData.put("cacheName", cacheName);

                    Cache springCache = cacheManager.getCache(cacheName);
                    if (springCache != null) {
                        springCache.getNativeCache().toString();
                        cacheData.put("entries", "Cache content available, but cannot be directly accessed. Type: "
                                + springCache.getNativeCache().getClass().getName());
                        return cacheData;
                    } else {
                        cacheData.put("entries", "Cache not found");
                    }

                    return cacheData;
                })
                .collect(Collectors.toList());
    }
}
