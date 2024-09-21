package com.ttalkak.deployment.deployment.framework.jpaadapter.adapter;

import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.outputport.HostingOutputPort;
import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import com.ttalkak.deployment.deployment.framework.jpaadapter.repository.HostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HostingAdapter implements HostingOutputPort {

    private final HostingRepository hostingRepository;

    @Override
    public HostingEntity save(HostingEntity hostingEntity) {
        return hostingRepository.save(hostingEntity);
    }

    @Override
    public Optional<HostingEntity> findById(Long hostingId) {
        return hostingRepository.findById(hostingId);
    }

    @Override
    public HostingEntity findByProjectIdAndServiceType(Long projectId, ServiceType serviceType) {
        return hostingRepository.findByProjectIdAndServiceType(projectId, serviceType)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTS_HOSTING));
    }
}
