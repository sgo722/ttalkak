package com.ttalkak.deployment.deployment.framework.web.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GithubRepositoryRequest {

    @NotNull(message = "깃허브 레포지토리 오너는 필수입니다.")
    private String repositoryOwner;

    @NotNull(message = "깃허브 레포지토리 이름은 필수입니다.")
    private String repositoryName;

    @NotNull(message = "깃허브 레포지토리 URL은 필수입니다.")
    private String repositoryUrl;

    @NotNull(message = "깃허브 레포지토리 루트 디렉토리는 필수입니다.")
    private String rootDirectory;

    @NotNull(message = "깃허브 레포지토리 브랜치는 필수입니다.")
    private String branch;

    @Builder
    private GithubRepositoryRequest(String repositoryOwner, String repositoryName, String repositoryUrl, String rootDirectory, String branch) {
        this.repositoryOwner = repositoryOwner;
        this.repositoryName = repositoryName;
        this.repositoryUrl = repositoryUrl;
        this.rootDirectory = rootDirectory;
        this.branch = branch;
    }
}
