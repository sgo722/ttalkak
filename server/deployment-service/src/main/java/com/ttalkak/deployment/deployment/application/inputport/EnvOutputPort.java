package com.ttalkak.deployment.deployment.application.inputport;

import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.domain.model.EnvEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvOutputPort {

    public EnvEntity save(EnvEntity envEntity);
}
