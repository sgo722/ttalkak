package com.ttalkak.deployment.deployment.framework.domainadapter;

import com.ttalkak.deployment.config.DomainFeignClient;
import com.ttalkak.deployment.deployment.application.outputport.DomainOutputPort;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DomainKeyResponse;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DomainRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DomainAdapterPort implements DomainOutputPort {

    private final DomainFeignClient domainFeignClient;

    @Override
    public DomainKeyResponse makeDomainKey(DomainRequest domainRequest) {
        return domainFeignClient.getDomainKey(domainRequest);
    }

    @Override
    public void deleteDomainKey(String identifier) {
        domainFeignClient.deleteDomainKey(identifier);
    }

    @Override
    public DomainKeyResponse updateDomainKey(DomainRequest domainRequest) {
        return domainFeignClient.updateDomainKey(domainRequest);
    }
}
