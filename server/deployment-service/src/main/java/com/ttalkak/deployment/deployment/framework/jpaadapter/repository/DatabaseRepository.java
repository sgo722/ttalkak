package com.ttalkak.deployment.deployment.framework.jpaadapter.repository;

import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseRepository extends JpaRepository<DatabaseEntity, Long> {


    @Query("select d from DatabaseEntity d where d.userId = :userId")
    List<DatabaseEntity> findAllByUserId(@Param("userId") Long userId);


    @Query("select p from DatabaseEntity p where p.userId =:userId")
    Page<DatabaseEntity> findAllByPaging(Pageable pageable, @Param("userId")Long userId);

    // 유저 Id, 키워드로 페이징처리
    @Query("select p from DatabaseEntity p where p.userId = :userId and lower(p.name) like lower(concat('%', :searchKeyword, '%') )")
    Page<DatabaseEntity> findDatabaseContainsSearchKeyWord(Pageable pageable, @Param("userId")Long userId, @Param("searchKeyword")String searchKeyword);
}
