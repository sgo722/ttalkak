package com.ttalkak.deployment.deployment.framework.jpaadapter.repository;

import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeploymentRepository extends JpaRepository<DeploymentEntity, Long> {

    @Query("select d from DeploymentEntity d where d.projectId = :projectId")
    List<DeploymentEntity> findAllByProjectId(@Param("projectId") Long projectId);

    @Query("select d from DeploymentEntity d where d.githubInfo.repositoryName like %:githubRepoName% ORDER BY d.id")
    List<DeploymentEntity> searchDeploymentByGithubRepoName(@Param("githubRepoName") String githubRepoName, Pageable pageable);

    @Query("select d from DeploymentEntity d where d.projectId = :projectId and d.serviceType = :serviceType ORDER BY d.id DESC")
    List<DeploymentEntity> findByProjectIdAndServiceType(@Param("projectId") Long projectId, @Param("serviceType") ServiceType serviceType);
}
