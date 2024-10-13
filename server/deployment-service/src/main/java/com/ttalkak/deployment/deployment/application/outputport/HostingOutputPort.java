package com.ttalkak.deployment.deployment.application.outputport;

import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostingOutputPort {
    public HostingEntity save(HostingEntity hostingEntity);

    public Optional<HostingEntity> findById(Long hostingId);

    HostingEntity findByProjectIdAndServiceType(Long projectId, ServiceType serviceType);
}
