package com.ttalkak.deployment.deployment.framework.domainadapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DomainKeyResponse {

    private String identifier;

    private String subdomain;

    private String key;
}
