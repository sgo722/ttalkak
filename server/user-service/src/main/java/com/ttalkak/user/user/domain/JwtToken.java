package com.ttalkak.user.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtToken {
    private String accessToken;
    private String refreshToken;
}
