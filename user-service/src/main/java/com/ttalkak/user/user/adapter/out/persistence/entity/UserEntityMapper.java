package com.ttalkak.user.user.adapter.out.persistence.entity;

import com.ttalkak.user.user.domain.LoginUser;
import com.ttalkak.user.user.domain.User;

public class UserEntityMapper {
    public static UserEntity toEntity(User user) {
        return null;
    }

    public static User toUser(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setPassword(userEntity.getPassword());
        user.setEmail(userEntity.getEmail());
        user.setGithubToken(userEntity.getGithubToken());
        user.setProfileImage(userEntity.getProfileImage());
        user.setEmailVerified(userEntity.isEmailVerified());
        user.setUserRole(userEntity.getUserRole());
        return user;
    }

    public static LoginUser toLoginUser(UserEntity userEntity) {
        LoginUser user = new LoginUser();
        user.setUserId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setEmail(userEntity.getEmail());
        user.setProfileImage(userEntity.getProfileImage());
        user.setEmailVerified(userEntity.isEmailVerified());
        user.setAccessToken(userEntity.getGithubToken());
        return user;
    }
}
