package com.ttalkak.deployment.deployment.application.usecase;


import com.ttalkak.deployment.common.UseCase;

public interface DeleteDatabaseUseCase {

    void deleteDatabase(Long userId, Long databaseId);
}
