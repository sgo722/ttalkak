package com.ttalkak.deployment.deployment.application.inputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.outputport.DeploymentOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.DomainOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.HostingOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.ProjectOutputPort;
import com.ttalkak.deployment.deployment.application.usecase.DeleteDeploymentUsecase;
import com.ttalkak.deployment.deployment.domain.event.CommandEvent;
import com.ttalkak.deployment.deployment.domain.event.UpdateDeploymentStatusEvent;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.DeploymentStatus;
import com.ttalkak.deployment.deployment.framework.kafka.ChangeStatusProducer;
import com.ttalkak.deployment.deployment.framework.projectadapter.dto.ProjectInfoResponse;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@Transactional
@RequiredArgsConstructor
public class DeleteDeploymentInputPort implements DeleteDeploymentUsecase {
    private final DeploymentOutputPort deploymentOutputPort;
    private final ProjectOutputPort projectOutputPort;
    private final DomainOutputPort domainOutputPort;
    private final HostingOutputPort hostingOutputPort;

    private final ChangeStatusProducer changeStatusProducer;

    @Override
    public void deleteDeployment(Long userId, Long deploymentId) {
        DeploymentEntity deploymentEntity = deploymentOutputPort.findDeployment(deploymentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_EXISTS_DEPLOYMENT));
        Long projectId = deploymentEntity.getProjectId();

        ProjectInfoResponse projectInfo = projectOutputPort.getProjectInfo(projectId);

        if(!Objects.equals(userId, projectInfo.getUserId())){
            throw new EntityNotFoundException(ErrorCode.UN_AUTHORIZATION);
        }


        UpdateDeploymentStatusEvent deleted = new UpdateDeploymentStatusEvent(deploymentId.toString(), CommandEvent.DELETE.toString());
        try{
            changeStatusProducer.occurUpdateDeploymentStatus(deleted);
        }catch (JsonProcessingException e){
            throw new BusinessException(ErrorCode.KAFKA_PRODUCER_ERROR);
        }
        HostingEntity findHosting = hostingOutputPort.findByProjectIdAndServiceType(deploymentEntity.getProjectId(), deploymentEntity.getServiceType());
        findHosting.delete();
        domainOutputPort.deleteDomainKey(findHosting.getId().toString());
        deploymentEntity.deleteDeployment();
        deploymentOutputPort.save(deploymentEntity);
    }

    @Override
    public void deleteDeploymentByProject(Long projectId) throws Exception {
        List<DeploymentEntity> deploymentEntities = deploymentOutputPort.findAllByProjectId(projectId);
        List<DeploymentEntity> deployments = deploymentEntities.stream()
                .filter(deploymentEntity -> DeploymentStatus.isAlive(deploymentEntity.getStatus()))
                .toList();

        for(DeploymentEntity deployment : deployments) {
            deployment.deleteDeployment();
            HostingEntity findHosting = hostingOutputPort.findByProjectIdAndServiceType(deployment.getProjectId(), deployment.getServiceType());
            findHosting.delete();
            domainOutputPort.deleteDomainKey(findHosting.getId().toString());
            deploymentOutputPort.save(deployment);
        }
    }
}
