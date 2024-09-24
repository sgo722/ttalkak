package com.ttalkak.user.user.adapter.out.persistence.repository;

import com.ttalkak.user.user.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    @Query("UPDATE UserEntity u SET u.githubToken = :accessToken WHERE u.username = :username")
    @Modifying
    void updateGithubToken(String username, String accessToken);

    @Query("UPDATE UserEntity u SET u.isEmailVerified = true, u.email = :email WHERE u.id = :userId")
    @Modifying
    void verifyEmail(Long userId, String email);
}
