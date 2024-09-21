package com.ttalkak.deployment.deployment.application.outputport;

import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DomainKeyResponse;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DomainRequest;

public interface DomainOutputPort {
    DomainKeyResponse makeDomainKey(DomainRequest domainRequest);

    void deleteDomainKey(String subDomain);

    DomainKeyResponse updateDomainKey(DomainRequest domainRequest);
}
