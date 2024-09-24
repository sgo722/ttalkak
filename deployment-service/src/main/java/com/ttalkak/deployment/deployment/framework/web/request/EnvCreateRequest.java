package com.ttalkak.deployment.deployment.framework.web.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EnvCreateRequest {

    private String key;

    private String value;


    @Builder
    private EnvCreateRequest(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
