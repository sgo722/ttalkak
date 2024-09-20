package com.ttalkak.deployment.deployment.domain.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DomainNameEvent {

    Long projectId;

    String orgDomainName;

    String newDomainName;
}
