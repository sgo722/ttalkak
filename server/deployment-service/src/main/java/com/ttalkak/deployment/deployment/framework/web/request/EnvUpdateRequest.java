package com.ttalkak.deployment.deployment.framework.web.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EnvUpdateRequest {

    private String key;

    private String value;
}
