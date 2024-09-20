package com.ttalkak.deployment.deployment.domain.model.vo;


import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.domain.model.EnvEntity;
import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DeploymentEditor {

    GithubInfo githubInfo;

    List<EnvEntity> envs;

    @Builder
    public DeploymentEditor(GithubInfo githubInfo, List<EnvEntity> envs) {
        this.githubInfo = githubInfo;
        this.envs = envs;
    }
}
