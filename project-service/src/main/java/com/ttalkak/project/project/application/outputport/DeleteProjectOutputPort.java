package com.ttalkak.project.project.application.outputport;

public interface DeleteProjectOutputPort {
    // 프로젝트 삭제
    void deleteProject(Long projectId);

    // 삭제된 프로젝트 롤백
    void rollbackStatusProject(Long projectId);
}
