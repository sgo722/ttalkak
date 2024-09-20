package com.ttalkak.user.user.domain;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String githubToken;
    private String username;
    private String password;
    private String email;
    private UserRole userRole;
    private String profileImage;
    private boolean isEmailVerified;
}
