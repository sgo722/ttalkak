package com.ttalkak.deployment.deployment.framework.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebHookCommit {
    private String message;
}
