package com.ttalkak.deployment.deployment.framework.domainadapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class DatabaseDomainKeyResponse {

    private String identifier;

    private String subdomain;

    private String key;

    private String port;
}
