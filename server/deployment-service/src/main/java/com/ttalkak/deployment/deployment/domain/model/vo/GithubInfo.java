package com.ttalkak.deployment.deployment.domain.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class GithubInfo {

    private String repositoryOwner;
    private String repositoryName;

    private String repositoryUrl;

    @Column(nullable = false)
    private String rootDirectory;

    @Column(nullable = false)
    private String branch;

    @Builder
    private GithubInfo(String repositoryOwner, String repositoryName, String repositoryUrl, String rootDirectory, String branch) {
        this.repositoryOwner = repositoryOwner;
        this.repositoryName = repositoryName;
        this.repositoryUrl = repositoryUrl;
        this.rootDirectory = rootDirectory;
        this.branch = branch;
    }

    public static GithubInfo create(String repositoryOwner, String repositoryName, String repositoryUrl, String rootDirectory, String branch) {
        return GithubInfo.builder()
                .repositoryName(repositoryName)
                .repositoryUrl(repositoryUrl)
                .repositoryOwner(repositoryOwner)
                .rootDirectory(rootDirectory)
                .branch(branch)
                .build();
    }
}
