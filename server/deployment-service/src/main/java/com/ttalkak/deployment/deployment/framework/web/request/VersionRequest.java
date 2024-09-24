package com.ttalkak.deployment.deployment.framework.web.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VersionRequest {

    @NotNull(message = "깃허브 레포지토리 마지막 커밋 메시지는 필수입니다.")
    private String repositoryLastCommitMessage;

    @NotNull(message = "깃허브 레포지토리 커밋 유저 이미지는 필수입니다.")
    private String repositoryLastCommitUserProfile;

    @NotNull(message = "깃허브 레포지토리 마지막 커밋 유저 이름은 필수입니다.")
    private String repositoryLastCommitUserName;

    @Builder
    public VersionRequest(String repositoryLastCommitMessage, String repositoryLastCommitUserProfile, String repositoryLastCommitUserName) {
        this.repositoryLastCommitMessage = repositoryLastCommitMessage;
        this.repositoryLastCommitUserProfile = repositoryLastCommitUserProfile;
        this.repositoryLastCommitUserName = repositoryLastCommitUserName;
    }
}
