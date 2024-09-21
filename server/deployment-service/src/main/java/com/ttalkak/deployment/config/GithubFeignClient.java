package com.ttalkak.deployment.config;

import com.ttalkak.deployment.deployment.framework.githubadpater.dto.GithubCommitResponse;
import com.ttalkak.deployment.deployment.framework.githubadpater.dto.GithubRepositoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "githubFeignClient", url = "https://api.github.com")
public interface GithubFeignClient {

    @GetMapping("/repos/{owner}/{repo}")
    GithubRepositoryResponse getRepositoryDetails(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo
    );

    @GetMapping("/repos/{owner}/{repo}/commits")
    List<GithubCommitResponse> getLastCommitDetails(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo,
            @RequestParam("per_page") int perPage
    );
}
