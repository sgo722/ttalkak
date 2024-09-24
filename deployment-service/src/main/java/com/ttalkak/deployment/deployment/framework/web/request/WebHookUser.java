package com.ttalkak.deployment.deployment.framework.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebHookUser {
    private String login;

    @JsonProperty("avatar_url")
    private String avatarUrl;
}
