package com.ttalkak.project.project.application.outputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.project.project.domain.event.DomainNameEvent;
import com.ttalkak.project.project.domain.event.ProjectEvent;

public interface EventOutputPort {

    // 프로젝트 삭제 카프카 메시지 전송
    public void occurDeleteDeploymentInstance(ProjectEvent projectEvent) throws JsonProcessingException;

    // 프로젝트 도메인명 변경 카프카 메시지 전송
    void occurUpdateHostingDomainName(DomainNameEvent domainNameEvent);
}
