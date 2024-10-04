package com.ttalkak.deployment.deployment.application.inputport;

import com.ttalkak.deployment.common.global.error.ErrorCode;
import com.ttalkak.deployment.common.global.exception.BusinessException;
import com.ttalkak.deployment.deployment.application.outputport.*;
import com.ttalkak.deployment.deployment.application.usecase.DeleteDatabaseUseCase;
import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeleteDatabaseInputPort implements DeleteDatabaseUseCase {

    private final DomainOutputPort domainOutputPort;

    private final DatabaseOutputPort databaseOutputPort;

    @Override
    public void deleteDatabase(Long userId, Long databaseId) {
        DatabaseEntity databaseEntity = databaseOutputPort.findById(databaseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXISTS_DATABASE));

        if(!databaseEntity.getUserId().equals(userId)){
            throw new BusinessException(ErrorCode.UN_AUTHORIZATION);
        }

        domainOutputPort.deleteDomainKey("database_" + databaseEntity.getId());

        databaseOutputPort.delete(databaseEntity);
    }
}
