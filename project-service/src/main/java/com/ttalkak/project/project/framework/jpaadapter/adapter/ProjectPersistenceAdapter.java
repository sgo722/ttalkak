package com.ttalkak.project.project.framework.jpaadapter.adapter;

import com.ttalkak.project.common.PersistenceAdapter;
import com.ttalkak.project.common.error.ErrorCode;
import com.ttalkak.project.common.exception.EntityNotFoundException;
import com.ttalkak.project.project.application.outputport.DeleteProjectOutputPort;
import com.ttalkak.project.project.application.outputport.LoadElasticSearchOutputPort;
import com.ttalkak.project.project.application.outputport.LoadProjectOutputPort;
import com.ttalkak.project.project.domain.model.LogEntryDocument;
import com.ttalkak.project.project.domain.model.ProjectEntity;
import com.ttalkak.project.project.domain.model.vo.ProjectStatus;
import com.ttalkak.project.project.framework.jpaadapter.repository.LogRepository;
import com.ttalkak.project.project.framework.jpaadapter.repository.ProjectJpaRepository;
import com.ttalkak.project.project.application.outputport.SaveProjectOutputPort;
import com.ttalkak.project.project.framework.web.request.SearchLogRequest;
import com.ttalkak.project.project.framework.web.response.LogPageResponse;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements SaveProjectOutputPort, 
        LoadProjectOutputPort,
        DeleteProjectOutputPort {
    private final ProjectJpaRepository projectJpaRepository;

    /**
     * 프로젝트 생성
     * @param projectEntity
     * @return
     */
    @Override
    public ProjectEntity save(ProjectEntity projectEntity) {

        return projectJpaRepository.save(projectEntity);
    }

    /**
     * 프로젝트 단건 조회
     * @param projectId
     * @return
     */
    @Override
    public ProjectEntity findById(Long projectId) {
        ProjectEntity projectEntity = projectJpaRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_EXISTS_PROJECT));

        if(projectEntity.getStatus() == ProjectStatus.DELETED) throw new EntityNotFoundException(ErrorCode.NOT_EXISTS_PROJECT);
        return projectEntity;
    }

    /**
     * 프로젝트 단순 페이징 처리
     * @param pageable
     * @param userId
     * @return
     */
    @Override
    public Page<ProjectEntity> findMyProjects(Pageable pageable, Long userId) {
        return projectJpaRepository.findMyProjects(pageable, userId);
    }

    /**
     * 프로젝트 키워드 포함 페이징 처리 
     * @param pageable
     * @param userId
     * @param searchKeyword
     * @return
     */
    @Override
    public Page<ProjectEntity> findMyPrjectsContinsSearchKeyWord(Pageable pageable, Long userId, String searchKeyword) {
        return projectJpaRepository.findMyProjectsContainsSearchKeyWord(pageable, userId, searchKeyword);
    }

    /**
     * 도메인명으로 엔티티 조회
     * @param domainName
     * @return
     */
    @Override
    public ProjectEntity findByDomainName(String domainName) {
        return projectJpaRepository.findByDomainName(domainName);
    }

    @Override
    public ProjectEntity findByWebHookToken(String webHookToken) {
        return projectJpaRepository.findByWebHookToken(webHookToken).orElseThrow(
                () -> new EntityNotFoundException(ErrorCode.NOT_EXISTS_PROJECT)
        );
    }


    /**
     * 프로젝트 삭제
     * @param projectId
     */
    @Override
    public void deleteProject(Long projectId) {
        ProjectEntity projectEntity = projectJpaRepository.findById(projectId)
                .orElseThrow( () -> new EntityNotFoundException(ErrorCode.NOT_EXISTS_PROJECT));
        
        // 이미 삭제된 프로젝트인 경우 삭제 과정을 진행하지 않는다.
        if(projectEntity.getStatus() == ProjectStatus.DELETED) throw new EntityNotFoundException(ErrorCode.NOT_EXISTS_PROJECT);

        projectEntity.updateDeletedStatus();
        projectJpaRepository.save(projectEntity);
    }

    /**
     * 삭제된 프로젝트 롤백
     * @param projectId
     */
    @Override
    public void rollbackStatusProject(Long projectId) {
        ProjectEntity projectEntity = projectJpaRepository.findById(projectId)
                .orElseThrow( () -> new EntityNotFoundException(ErrorCode.NOT_EXISTS_PROJECT));

        projectEntity.rollbackDeletedStatus();
        projectJpaRepository.save(projectEntity);
    }
}
