package com.ttalkak.deployment.deployment.domain.model.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DatabaseEditor {

    private int port;

    private String username;

    private String password;

    @Builder
    public DatabaseEditor(int port, String username, String password) {
        this.port = port;
        this.username = username;
        this.password = password;
    }
}
