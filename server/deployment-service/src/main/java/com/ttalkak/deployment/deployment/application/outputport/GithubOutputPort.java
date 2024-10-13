package com.ttalkak.deployment.deployment.application.outputport;

import com.ttalkak.deployment.deployment.domain.model.vo.GithubInfo;
import com.ttalkak.deployment.deployment.domain.model.vo.GithubRepository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface GithubOutputPort {
    public GithubRepository getRepositoryDetails(String owner, String repo);

    public GithubInfo getLastCommitDetails(String owner, String repo);

    public Map<String, Object> createDockerFile(String token,
                                                String owner,
                                                String repo,
                                                String path,
                                                Map<String, Object> body);
}
