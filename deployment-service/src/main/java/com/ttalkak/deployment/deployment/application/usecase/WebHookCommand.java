package com.ttalkak.deployment.deployment.application.usecase;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebHookCommand {
    private String repositoryName;
    private String repositoryUrl;
    private String commitUsername;
    private String commitUserProfile;
    private String commitMessage;
}
