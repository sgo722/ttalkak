package com.ttalkak.deployment.deployment.application.inputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.common.global.exception.EntityNotFoundException;
import com.ttalkak.deployment.deployment.application.outputport.DeploymentOutputPort;
import com.ttalkak.deployment.deployment.application.usecase.CommandDeploymentStatusUsecase;
import com.ttalkak.deployment.deployment.domain.event.UpdateDeploymentStatusEvent;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.DeploymentStatus;
import com.ttalkak.deployment.deployment.framework.kafka.ChangeStatusProducer;
import com.ttalkak.deployment.deployment.framework.web.request.DeploymentCommandStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CommandDeploymentStatusInputPort implements CommandDeploymentStatusUsecase {


    private final DeploymentOutputPort deploymentOutputPort;

    private final ChangeStatusProducer changeStatusProducer;


    @Override
    public void commandDeploymentStatus(DeploymentCommandStatusRequest deploymentCommandStatusRequest){
        DeploymentEntity deploymentEntity = deploymentOutputPort.findDeployment(Long.valueOf(deploymentCommandStatusRequest.getDeploymentId()))
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_EXISTS_DEPLOYMENT));
            deploymentEntity.setStatus(DeploymentStatus.PENDING);
            UpdateDeploymentStatusEvent updateDeploymentStatusEvent = toKafkaEventMessage(deploymentCommandStatusRequest);
            try{
                changeStatusProducer.occurUpdateDeploymentStatus(updateDeploymentStatusEvent);
            }catch (JsonProcessingException e){
                throw new BusinessException(ErrorCode.KAFKA_PRODUCER_ERROR);
            }
        deploymentOutputPort.save(deploymentEntity);
    }

    private UpdateDeploymentStatusEvent toKafkaEventMessage(DeploymentCommandStatusRequest deploymentUpdateStatusRequest) {
        UpdateDeploymentStatusEvent updateDeploymentStatusEvent = new UpdateDeploymentStatusEvent(
                deploymentUpdateStatusRequest.getDeploymentId().toString(),
                deploymentUpdateStatusRequest.getCommand().toString()
        );
        return updateDeploymentStatusEvent;
    }

}
