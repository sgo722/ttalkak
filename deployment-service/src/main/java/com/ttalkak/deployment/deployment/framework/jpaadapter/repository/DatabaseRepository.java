package com.ttalkak.deployment.deployment.framework.jpaadapter.repository;

import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseRepository extends JpaRepository<DatabaseEntity, Long> {


    @Query("select d from DatabaseEntity d where d.userId = :projectId")
    List<DatabaseEntity> findAllByUserId(@Param("userId") Long userId);

    @Query("select d from DatabaseEntity d where d.id = :databaseId")
    DatabaseEntity findByDatabaseId(@Param("databaseId") Long databaseId);

}
