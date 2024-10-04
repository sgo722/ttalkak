package com.ttalkak.deployment.deployment.application.outputport;

import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DatabaseDomainKeyRequest;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DatabaseDomainKeyResponse;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.WebDomainKeyResponse;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.WebDomainRequest;

public interface DomainOutputPort {
    WebDomainKeyResponse makeDomainKey(WebDomainRequest webDomainRequest);

    void deleteDomainKey(String subDomain);

    WebDomainKeyResponse updateDomainKey(WebDomainRequest webDomainRequest);

    DatabaseDomainKeyResponse makeDatabaseDomainKey(DatabaseDomainKeyRequest databaseDomainKeyRequest);
}
