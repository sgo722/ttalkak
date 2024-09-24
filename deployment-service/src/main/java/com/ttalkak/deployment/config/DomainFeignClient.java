package com.ttalkak.deployment.config;

import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DomainKeyResponse;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DomainRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "DOMAIN-SERVICE", url="${subdomain.endpoint}")
public interface DomainFeignClient {
    @PostMapping("/create")
    DomainKeyResponse getDomainKey(@RequestBody DomainRequest domainRequest);

    @PostMapping("/delete/{identifier}")
    void deleteDomainKey(@PathVariable("identifier") String identifier);

    @PostMapping("update")
    DomainKeyResponse updateDomainKey(@RequestBody DomainRequest domainRequest);
}
