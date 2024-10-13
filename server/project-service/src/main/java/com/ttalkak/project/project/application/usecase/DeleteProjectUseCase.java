package com.ttalkak.project.project.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface DeleteProjectUseCase {
    void deleteProject(Long userId, Long projectId) throws JsonProcessingException;

    void rollbackStatusProject(Long projectId);
}
