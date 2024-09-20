package com.ttalkak.project.project.framework.web;

import com.ttalkak.project.common.WebAdapter;
import com.ttalkak.project.project.application.usecase.GetProjectUseCase;
import com.ttalkak.project.project.framework.web.response.ProjectResponse;
import com.ttalkak.project.project.framework.web.response.ProjectWebHookResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/feign/project")
public class ProjectFeignController {
    private final GetProjectUseCase getProjectUseCase;

    /**
     * 프로젝트 페인 단건 조회
     * @param projectId
     * @return
     */
    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse getFeignProject(@PathVariable Long projectId) {
        ProjectResponse projectResponse = getProjectUseCase.getFeignProject(projectId);
        return projectResponse;
    }

    @GetMapping("/webhook/{webhookToken}")
    public ProjectWebHookResponse projectWebHook(
            @PathVariable String webhookToken
    ){
        return getProjectUseCase.getWebHookProject(webhookToken);
    }
}
