package com.ttalkak.deployment.deployment.framework.jpaadapter.adapter;

import com.ttalkak.deployment.deployment.application.outputport.VersionOutputPort;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.VersionEntity;
import com.ttalkak.deployment.deployment.framework.jpaadapter.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VersionAdapter implements VersionOutputPort {

    private final VersionRepository versionRepository;

    @Override
    public VersionEntity save(VersionEntity versionEntity) {
        return versionRepository.save(versionEntity);
    }


    @Override
    public VersionEntity findById(Long versionId) {
        VersionEntity versionEntity = versionRepository.findLastVersionById(versionId);
        return versionEntity;
    }

    @Override
    public VersionEntity findLastVersionByDeploymentId(Long deploymentId) {
        return versionRepository.findLastVersionByDeploymentId(deploymentId);
    }

    @Override
    public List<VersionEntity> findAllByDeploymentId(DeploymentEntity deploymentEntity) {
        return versionRepository.findAllByDeploymentId(deploymentEntity);
    }
}
