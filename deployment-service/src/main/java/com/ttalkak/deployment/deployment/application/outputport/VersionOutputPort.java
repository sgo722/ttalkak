package com.ttalkak.deployment.deployment.application.outputport;

import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.VersionEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VersionOutputPort {

    VersionEntity save(VersionEntity versionEntity);

    VersionEntity findById(Long versionId);

    List<VersionEntity> findAllByDeploymentId(DeploymentEntity deploymentEntity);
}
