package com.ttalkak.project.project.framework.deploymentadapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HostingResponse {

    private Long hostingId;

    private String detailDomainName;

    private String serviceType;

    private int hostingPort;

    @Builder
    public HostingResponse(Long hostingId, String detailDomainName, String serviceType, int hostingPort) {
        this.hostingId = hostingId;
        this.detailDomainName = detailDomainName;
        this.serviceType = serviceType;
        this.hostingPort = hostingPort;
    }
}
