package com.ttalkak.deployment.deployment.framework.githubadpater.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GithubRepositoryResponse {

    @JsonProperty("name")
    private String repositoryName;


    @JsonProperty("svn_url")
    private String repositoryUrl;
}
