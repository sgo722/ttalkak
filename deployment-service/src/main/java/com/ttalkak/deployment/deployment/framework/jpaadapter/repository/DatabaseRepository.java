package com.ttalkak.deployment.deployment.framework.jpaadapter.repository;

import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseRepository extends JpaRepository<DatabaseEntity, Long> {
}
