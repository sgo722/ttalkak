package com.ttalkak.deployment.deployment.domain.event;

import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDeploymentStatusEvent implements Serializable {

    private Long id;

    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    private CommandEvent command;
}
