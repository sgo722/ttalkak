package com.ttalkak.project.project.application.inputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.project.common.UseCase;
import com.ttalkak.project.common.error.ErrorCode;
import com.ttalkak.project.common.exception.BusinessException;
import com.ttalkak.project.project.application.outputport.DeleteProjectOutputPort;
import com.ttalkak.project.project.application.outputport.EventOutputPort;
import com.ttalkak.project.project.application.outputport.LoadProjectOutputPort;
import com.ttalkak.project.project.application.usecase.DeleteProjectUseCase;
import com.ttalkak.project.project.domain.event.ProjectEvent;
import com.ttalkak.project.project.domain.model.ProjectEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@UseCase
@RequiredArgsConstructor
public class DeleteProjectInputPort implements DeleteProjectUseCase {
    private final DeleteProjectOutputPort deleteProjectOutputPort;
    private final LoadProjectOutputPort loadProjectOutputPort;
    private final EventOutputPort eventOutputPort;

    /**
     * 프로젝트 삭제
     *
     * @param projectId 프로젝트 아이디
     */
    @Override
    public void deleteProject(Long userId, Long projectId) throws JsonProcessingException {
        ProjectEntity projectEntity = loadProjectOutputPort.findById(projectId);
        // 유저 프로젝트가 아닌 경우 예외 발생
        if(userId != projectEntity.getUserId()) throw new BusinessException(ErrorCode.ACCESS_PROJECT_DENIED);

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
