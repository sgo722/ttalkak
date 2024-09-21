package com.ttalkak.deployment.deployment.framework.jpaadapter.adapter;

import com.ttalkak.deployment.deployment.application.outputport.DeploymentOutputPort;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.framework.jpaadapter.repository.DeploymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class DeploymentAdapter implements DeploymentOutputPort {

    private final DeploymentRepository deploymentRepository;

    @Override
    public List<DeploymentEntity> findAllByProjectId(Long projectId) {
        return deploymentRepository.findAllByProjectId(projectId);
    }

    @Override
    public List<DeploymentEntity> searchDeploymentByGithubRepoName(String githubRepoName, int page, int size) {
        PageRequest paging = PageRequest.of(page, size);
        return deploymentRepository.searchDeploymentByGithubRepoName(githubRepoName, (Pageable) paging);
    }

    @Override
    public Optional<DeploymentEntity> findDeployment(Long deploymentId) {
        return deploymentRepository.findById(deploymentId);
    }

    @Override
    public DeploymentEntity save(DeploymentEntity deploymentEntity) {
        return deploymentRepository.save(deploymentEntity);
    }

    @Override
    public void delete(DeploymentEntity deploymentEntity) {
        deploymentRepository.delete(deploymentEntity);
    }

    @Override
    public void saveAll(List<DeploymentEntity> deploymentEntities) {
        deploymentRepository.saveAll(deploymentEntities);
    }

    @Override
    public Optional<DeploymentEntity> findByProjectIdAndServiceType(Long projectId, String serviceType) {
        return deploymentRepository.findByProjectIdAndServiceType(projectId, serviceType).stream().findAny();
    }
}
