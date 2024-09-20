package com.ttalkak.project.project.application.inputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.project.common.UseCase;
import com.ttalkak.project.project.application.outputport.DeleteProjectOutputPort;
import com.ttalkak.project.project.application.outputport.EventOutputPort;
import com.ttalkak.project.project.application.usecase.DeleteProjectUseCase;
import com.ttalkak.project.project.domain.event.ProjectEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@UseCase
@RequiredArgsConstructor
public class DeleteProjectInputPort implements DeleteProjectUseCase {
    private final DeleteProjectOutputPort deleteProjectOutputPort;
    private final EventOutputPort eventOutputPort;

    /**
     * 프로젝트 삭제
     *
     * @param projectId 프로젝트 아이디
     */
    @Override
    public void deleteProject(Long projectId) throws JsonProcessingException {
        deleteProjectOutputPort.deleteProject(projectId);
        eventOutputPort.occurDeleteDeploymentInstance(new ProjectEvent(projectId));
    }

    /**
     * 프로젝트 삭제 상태 롤백
     * @param projectId
     */
    @Override
    public void rollbackStatusProject(Long projectId) {
        deleteProjectOutputPort.rollbackStatusProject(projectId);
    }
}
