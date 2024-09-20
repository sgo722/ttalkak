package com.ttalkak.project.project.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DomainNameEvent {

    Long projectId;
    String orgDomainName;
    String newDomainName;
}
