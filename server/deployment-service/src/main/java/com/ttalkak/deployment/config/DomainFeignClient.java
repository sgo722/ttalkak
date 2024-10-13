package com.ttalkak.deployment.config;

import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DatabaseDomainKeyRequest;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DatabaseDomainKeyResponse;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.WebDomainKeyResponse;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.WebDomainRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "DOMAIN-SERVICE", url="${subdomain.endpoint}")
public interface DomainFeignClient {
    @PostMapping("/create")
    WebDomainKeyResponse getDomainKey(@RequestBody WebDomainRequest webDomainRequest);

    @PostMapping("/delete/{identifier}")
    void deleteDomainKey(@PathVariable("identifier") String identifier);

    @PostMapping("/update")
    WebDomainKeyResponse updateDomainKey(@RequestBody WebDomainRequest webDomainRequest);

    @PostMapping("/db/create")
    DatabaseDomainKeyResponse getDatabaseKey(@RequestBody DatabaseDomainKeyRequest databaseDomainKeyRequest);
}
