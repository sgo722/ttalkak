package com.ttalkak.user.user.adapter.in.stream.request;

import lombok.Data;

@Data
public class EmailVerifyRequest {
    private Long userId;
    private String email;
}
