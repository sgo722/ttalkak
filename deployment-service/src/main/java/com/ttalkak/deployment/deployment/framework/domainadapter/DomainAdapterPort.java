package com.ttalkak.deployment.deployment.framework.domainadapter;

import com.ttalkak.deployment.config.DomainFeignClient;
import com.ttalkak.deployment.deployment.application.outputport.DomainOutputPort;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DatabaseDomainKeyRequest;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DatabaseDomainKeyResponse;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.WebDomainKeyResponse;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.WebDomainRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainAdapterPort implements DomainOutputPort {

    private final DomainFeignClient domainFeignClient;

    @Override
    public WebDomainKeyResponse makeDomainKey(WebDomainRequest webDomainRequest) {
        return domainFeignClient.getDomainKey(webDomainRequest);
    }

    @Override
    public void deleteDomainKey(String identifier) {
        domainFeignClient.deleteDomainKey(identifier);
    }

    @Override
    public WebDomainKeyResponse updateDomainKey(WebDomainRequest webDomainRequest) {
        return domainFeignClient.updateDomainKey(webDomainRequest);
    }

    @Override
    public DatabaseDomainKeyResponse makeDatabaseDomainKey(DatabaseDomainKeyRequest databaseDomainKeyRequest) {
        return domainFeignClient.getDatabaseKey(databaseDomainKeyRequest);
    }
}
