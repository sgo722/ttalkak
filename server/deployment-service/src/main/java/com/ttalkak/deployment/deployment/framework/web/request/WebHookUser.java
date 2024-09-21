package com.ttalkak.deployment.deployment.framework.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WebHookUser {
    private String login;

    @JsonProperty("avatar_url")
    private String avatarUrl;
}
