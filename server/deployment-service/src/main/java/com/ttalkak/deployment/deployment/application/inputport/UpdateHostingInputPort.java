package com.ttalkak.deployment.deployment.application.inputport;

import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.outputport.DeploymentOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.DomainOutputPort;
import com.ttalkak.deployment.deployment.application.outputport.HostingOutputPort;
import com.ttalkak.deployment.deployment.application.usecase.UpdateHostingUseCase;
import com.ttalkak.deployment.deployment.domain.event.HostingEvent;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.EntityNotFoundException;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.WebDomainRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateHostingInputPort implements UpdateHostingUseCase {

    private final HostingOutputPort hostingOutputPort;

    private final DeploymentOutputPort deploymentOutputPort;

    private final DomainOutputPort domainOutputPort;

    @Override
    public void updateHosting(HostingEvent hostingEvent) {
        HostingEntity findHosting = hostingOutputPort.findById(hostingEvent.getHostingId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_EXISTS_HOSTING));

        findHosting.setDeployerId(hostingEvent.getDeployerId());
        hostingOutputPort.save(findHosting);
    }

    @Override
    public void updateHostingDomainName(Long projectId, String newDomainName) {
        List<DeploymentEntity> allDeploymentEntityByProjectId = deploymentOutputPort.findAllByProjectId(projectId);
        if(!allDeploymentEntityByProjectId.isEmpty()) {
            for (DeploymentEntity deploymentEntity : allDeploymentEntityByProjectId) {
                HostingEntity findHosting = hostingOutputPort.findByProjectIdAndServiceType(projectId, deploymentEntity.getServiceType());
                if (findHosting == null) {
                    throw new BusinessException(ErrorCode.NOT_EXISTS_HOSTING);
                }
                findHosting.updateDomainName(newDomainName,deploymentEntity.getServiceType());
                domainOutputPort.updateDomainKey(new WebDomainRequest(
                        findHosting.getId().toString(),
                        newDomainName + " " + findHosting.getServiceType().toString(),
                        findHosting.getDetailSubDomainName()));
                hostingOutputPort.save(findHosting);
            }
        }
    }
}
