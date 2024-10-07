package com.ttalkak.deployment.deployment.framework.web.request;

import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import com.ttalkak.deployment.deployment.domain.model.vo.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
public class UpdateStatusRequest {

    private Long id;

    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String message;
}
