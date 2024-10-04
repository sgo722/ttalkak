package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.common.UseCase;
import com.ttalkak.deployment.deployment.domain.event.HostingEvent;


@UseCase
public interface UpdateHostingUseCase {
    // 호스팅 정보 변경
    void updateHosting(HostingEvent hostingEvent);

    // 호스팅 도메인명 변경
    void updateHostingDomainName(Long projectId, String newDomainName);
}
