package com.ttalkak.project.project.framework.jpaadapter.repository;

import com.ttalkak.project.project.domain.model.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, Long> {
    // 유저 Id로 페이징 처리
    @Query("select p from ProjectEntity p where p.userId =:userId and p.status = 'ACTIVE' ")
    Page<ProjectEntity> findMyProjects(Pageable pageable, @Param("userId")Long userId);

    // 유저 Id, 키워드로 페이징처리
    @Query("select p from ProjectEntity p where p.userId = :userId and lower(p.projectName) like lower(concat('%', :searchKeyword, '%') ) and p.status = 'ACTIVE' ")
    Page<ProjectEntity> findMyProjectsContainsSearchKeyWord(Pageable pageable, @Param("userId")Long userId, @Param("searchKeyword")String searchKeyword);

    // 도메인 명으로 프로젝트 조회
    @Query("select p from ProjectEntity p where p.domainName like :domainName and p.status = 'ACTIVE' ")
    ProjectEntity findByDomainName(@Param("domainName") String domainName);

    @Query("SELECT p FROM ProjectEntity p WHERE p.webhookToken = :webhookToken")
    Optional<ProjectEntity> findByWebHookToken(@Param("webhookToken") String webhookToken);
}
