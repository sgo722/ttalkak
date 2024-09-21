package com.ttalkak.deployment.deployment.framework.jpaadapter.adapter;

import com.ttalkak.deployment.deployment.application.inputport.EnvOutputPort;
import com.ttalkak.deployment.deployment.domain.model.EnvEntity;
import com.ttalkak.deployment.deployment.framework.jpaadapter.repository.EnvJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class EnvAdapter implements EnvOutputPort {

    private final EnvJpaRepository envJpaRepository;

    @Override
    public EnvEntity save(EnvEntity envEntity) {
        return envJpaRepository.save(envEntity);
    }
}
