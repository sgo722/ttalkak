package com.ttalkak.deployment.deployment.framework.domainadapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DatabaseDomainKeyRequest {

    private String identifier;

    @JsonProperty("display_name")
    private String displayName;

    private String subdomain;
}
