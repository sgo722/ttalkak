package com.ttalkak.deployment.deployment.framework.githubadpater.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GithubCommitResponse {

    private Commit commit;

    private Committer committer;

    @Getter
    public static class Commit {
        private String message;
    }

    @Getter
    public static class Committer {
        @JsonProperty("avatar_url")
        private String avatarUrl;

        private String login;
    }

    public String getRepositoryLastCommitMessage() {
        return commit.getMessage();
    }

    public String getRepositoryLastCommitUserProfile() {
        return committer.getAvatarUrl();
    }

    public String getRepositoryLastCommitUserName() {
        return committer.getLogin();
    }
}
