package com.ttalkak.project.project.framework.web.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DomainNameRequest {
    String domainName;

    public DomainNameRequest(String domainName) {
        this.domainName = domainName;
    }
}
