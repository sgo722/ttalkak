package com.ttalkak.deployment.deployment.application.outputport;

import com.ttalkak.deployment.deployment.domain.model.vo.GithubInfo;
import com.ttalkak.deployment.deployment.domain.model.vo.GithubRepository;

public interface GithubOutputPort {
    public GithubRepository getRepositoryDetails(String owner, String repo);

    public GithubInfo getLastCommitDetails(String owner, String repo);
}
