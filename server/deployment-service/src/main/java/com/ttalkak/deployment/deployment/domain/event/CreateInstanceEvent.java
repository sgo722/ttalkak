package com.ttalkak.deployment.deployment.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInstanceEvent implements Serializable {
    private String deploymentId;

    private String port;

    private String subdomainName;

    private String subdomainKey;

    private String serviceType;

    private String branch;

    private String repositoryUrl;

    private String rootDirectory;

    private String expirationDate;

    private List<EnvEvent> envs;

    private List<DatabaseEvent> databases;

    private Long version;

    public CreateInstanceEvent(DeploymentEvent deployment, HostingEvent hosting, GithubInfoEvent githubInfo, List<EnvEvent> envs, List<DatabaseEvent> database, Long version, String expirationDate) {
        this.deploymentId = deployment.getDeploymentId().toString();
        this.port = String.valueOf(hosting.getHostingPort());
        this.subdomainName = hosting.getSubdomainName();
        this.subdomainKey = hosting.getSubdomainKey();
        this.serviceType = deployment.getServiceType();
        this.branch = githubInfo.getBranch();
        this.repositoryUrl = githubInfo.getRepositoryUrl();
        this.rootDirectory = githubInfo.getRootDirectory();
        this.envs = envs;
        this.databases = database;
        this.version = version;
        this.expirationDate = expirationDate;
    }
}
