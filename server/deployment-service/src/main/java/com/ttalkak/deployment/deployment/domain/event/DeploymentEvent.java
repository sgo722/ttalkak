package com.ttalkak.deployment.deployment.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeploymentEvent {

    private Long deploymentId;

    private Long projectId;

    private List<EnvEvent> envs;

    private String serviceType;
}
