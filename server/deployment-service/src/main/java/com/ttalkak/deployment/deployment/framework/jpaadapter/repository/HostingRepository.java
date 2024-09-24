package com.ttalkak.deployment.deployment.framework.jpaadapter.repository;

import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostingRepository extends JpaRepository<HostingEntity, Long> {
    @Query("SELECT h FROM HostingEntity h WHERE h.projectId = :projectId AND h.serviceType = :serviceType")
    Optional<HostingEntity> findByProjectIdAndServiceType(Long projectId, ServiceType serviceType);
}
