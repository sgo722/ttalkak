package com.ttalkak.deployment.deployment.application.usecase;

import com.ttalkak.deployment.deployment.framework.web.request.DatabaseUpdateStatusRequest;
import com.ttalkak.deployment.deployment.framework.web.request.UpdateStatusRequest;

public interface UpdateDatabaseStatusUseCase {

    void updateDatabaseStatus(UpdateStatusRequest updateStatusRequest);
}
