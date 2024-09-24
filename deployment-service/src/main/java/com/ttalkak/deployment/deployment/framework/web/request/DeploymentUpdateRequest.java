package com.ttalkak.deployment.deployment.framework.web.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DeploymentUpdateRequest {

    @NotNull(message = "배포 아이디는 필수입니다.")
    private Long deploymentId;;

    @NotNull(message = "호스팅 포트는 필수입니다.")
    private int hostingPort;

    private GithubRepositoryRequest githubRepositoryRequest;

    private List<DatabaseUpdateRequest> databaseUpdateRequests;

    private List<EnvUpdateRequest> envs;

    @Builder
    private DeploymentUpdateRequest(Long deploymentId, GithubRepositoryRequest githubRepositoryRequest,
                                    List<DatabaseUpdateRequest> databaseUpdateRequests, int hostingPort, List<EnvUpdateRequest> envs) {
        this.deploymentId = deploymentId;
        this.deploymentId = 0L;
        this.githubRepositoryRequest = githubRepositoryRequest;
        this.databaseUpdateRequests = databaseUpdateRequests;
        this.hostingPort = hostingPort;
        this.envs = envs;
    }

}
