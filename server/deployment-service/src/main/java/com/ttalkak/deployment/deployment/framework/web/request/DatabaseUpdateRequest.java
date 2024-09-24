package com.ttalkak.deployment.deployment.framework.web.request;

import com.ttalkak.deployment.deployment.domain.model.vo.DatabaseType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DatabaseUpdateRequest {

    private Long id;

    private String databaseType;

    private String username;

    private String password;

    private int port;
}
