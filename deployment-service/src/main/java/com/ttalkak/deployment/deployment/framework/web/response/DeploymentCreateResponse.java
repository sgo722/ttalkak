package com.ttalkak.deployment.deployment.framework.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class DeploymentCreateResponse {
    private String webhookUrl;
}
