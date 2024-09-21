package com.ttalkak.deployment.deployment.framework.web.request;

import com.ttalkak.deployment.deployment.domain.model.vo.DatabaseType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DatabaseCreateRequest {
    @NotNull(message = "데이터베이스 이름은 필수입니다.")
    @Enumerated(EnumType.STRING)
    private DatabaseType databaseName;

    @NotNull(message = "데이터베이스 포트는 필수입니다.")
    private int databasePort;

    private String username;

    @NotNull(message = "비밀번호는 필수입니다.")
    private String password;

    @Builder
    private DatabaseCreateRequest(DatabaseType databaseName, int databasePort, String username, String password) {
        this.databaseName = databaseName;
        this.databasePort = databasePort;
        this.username = username;
        this.password = password;
    }
}
