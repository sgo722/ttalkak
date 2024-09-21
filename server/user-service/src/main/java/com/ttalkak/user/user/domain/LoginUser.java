package com.ttalkak.user.user.domain;

import lombok.Data;

@Data
public class LoginUser {
    private Long userId;
    private String username;
    private String accessToken;
    private String email;
    private String profileImage;
    private boolean isEmailVerified;
}
