package com.ttalkak.user.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class UserCreateEvent {
    private Long userId;
    private String username;
    private String email;
    private String address;
}
