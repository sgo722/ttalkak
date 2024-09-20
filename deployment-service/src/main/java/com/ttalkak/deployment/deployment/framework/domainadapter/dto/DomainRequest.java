package com.ttalkak.deployment.deployment.framework.domainadapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DomainRequest {

    private String identifier;

    @JsonProperty("display_name")
    private String displayName;

    private String subdomain;
}
