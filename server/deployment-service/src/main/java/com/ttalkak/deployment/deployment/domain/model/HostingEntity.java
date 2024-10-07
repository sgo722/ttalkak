package com.ttalkak.deployment.deployment.domain.model;

import jakarta.persistence.*;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class HostingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private int hostingPort;

    // 처음 호스팅객체가 생성되었을때는 제공자와 호스팅아이피가 null값이어야함.
    @Setter
    @Column(name = "user_id", nullable = true)
    private Long deployerId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    private String detailSubDomainName;

    @Setter
    private String detailSubDomainKey;

    @Builder
    private HostingEntity(int hostingPort, Long deployerId, Long projectId, ServiceType serviceType, String detailSubDomainName, String detailSubDomainKey) {
        this.hostingPort = hostingPort;
        this.deployerId = deployerId;
        this.projectId = projectId;
        this.serviceType = serviceType;
        this.detailSubDomainName = detailSubDomainName;
        this.detailSubDomainKey = detailSubDomainKey;
    }

    public static HostingEntity createHosting(
            int hostingPort,
            Long projectId,
            String serviceType,
            String projectDomainName
    ){
        return HostingEntity.builder()
                .hostingPort(hostingPort)
                .projectId(projectId)
                .serviceType(ServiceType.valueOf(serviceType))
                .detailSubDomainName(changeDetailDomainName(projectDomainName, serviceType))
                .build();
    }

    private static String changeDetailDomainName(String projectDomainName, String serviceType) {
        if(ServiceType.isBackendType(serviceType)){
            return "api" + projectDomainName;
        }

        else if(ServiceType.isFrontendType(serviceType)){
            return projectDomainName;
        }

        throw new IllegalArgumentException("ServiceType이 잘못 입력되었습니다.");
    }

    public void updateDomainName(String domainName, String serviceType) {
        this.detailSubDomainName = changeDetailDomainName(domainName, serviceType);
    }


    public void delete() {
        this.serviceType = null;
    }
}
