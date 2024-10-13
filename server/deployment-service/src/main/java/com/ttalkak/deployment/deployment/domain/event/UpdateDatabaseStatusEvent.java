package com.ttalkak.deployment.deployment.domain.event;

import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDatabaseStatusEvent implements Serializable {

    private Long id;

    private ServiceType serviceType;

    private CommandEvent command;
}
