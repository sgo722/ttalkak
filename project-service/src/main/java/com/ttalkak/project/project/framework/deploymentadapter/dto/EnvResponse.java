package com.ttalkak.project.project.framework.deploymentadapter.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EnvResponse {

    private String envId;

    private String key;

    private String value;

    @Builder
    public EnvResponse(String envId, String key, String value) {
        this.envId = envId;
        this.key = key;
        this.value = value;
    }
}
