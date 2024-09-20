package com.ttalkak.user.user.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterCommand {
    private String username;
    private String password;
    private String email;
}
