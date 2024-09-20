package com.ttalkak.deployment.deployment.framework.web.response;

import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class HostingResponse {

    private Long hostingId;

    private String detailDomainName;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    private int hostingPort;

    @Builder
    private HostingResponse(Long hostingId, String detailDomainName, ServiceType serviceType, int hostingPort) {
        this.hostingId = hostingId;
        this.detailDomainName = detailDomainName;
        this.serviceType = serviceType;
        this.hostingPort = hostingPort;
    }

    public static HostingResponse mapToDTO(HostingEntity hostingEntity){
        return HostingResponse.builder()
                .hostingId(hostingEntity.getId())
                .serviceType(hostingEntity.getServiceType())
                .detailDomainName(hostingEntity.getDetailSubDomainName())
                .hostingPort(hostingEntity.getHostingPort())
                .build();
    }
}
