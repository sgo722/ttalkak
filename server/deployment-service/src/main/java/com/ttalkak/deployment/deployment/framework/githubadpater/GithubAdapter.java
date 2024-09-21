package com.ttalkak.deployment.deployment.framework.githubadpater;

import com.ttalkak.deployment.config.GithubFeignClient;
import com.ttalkak.deployment.deployment.application.outputport.GithubOutputPort;
import com.ttalkak.deployment.deployment.domain.model.vo.GithubInfo;
import com.ttalkak.deployment.deployment.domain.model.vo.GithubRepository;
import com.ttalkak.deployment.deployment.framework.githubadpater.dto.GithubCommitResponse;
import com.ttalkak.deployment.deployment.framework.githubadpater.dto.GithubRepositoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubAdapter implements GithubOutputPort {

    private final GithubFeignClient githubFeignClient;

    @Override
    public GithubRepository getRepositoryDetails(String owner, String repo) {
        GithubRepositoryResponse repositoryDetailsDTO = githubFeignClient.getRepositoryDetails(owner, repo);
        System.out.println(repositoryDetailsDTO.getRepositoryName());
        System.out.println(repositoryDetailsDTO.getRepositoryUrl());


        return new GithubRepository(repositoryDetailsDTO.getRepositoryName(), repositoryDetailsDTO.getRepositoryUrl());
    }

    @Override
    public GithubInfo getLastCommitDetails(String owner, String repo) {
        List<GithubCommitResponse> lastCommitDetailsDTO = githubFeignClient.getLastCommitDetails(owner, repo, 1);
        GithubCommitResponse githubCommitResponse = lastCommitDetailsDTO.get(0);

        System.out.println(githubCommitResponse.getCommit().getMessage());
        System.out.println(githubCommitResponse.getCommitter().getAvatarUrl());
        System.out.println(githubCommitResponse.getCommitter().getLogin());

        return null;
    }
}
