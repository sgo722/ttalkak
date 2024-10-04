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
    private DatabaseType type;

    private String name;

    @Builder
    private DatabaseCreateRequest(DatabaseType type,String name) {
        this.type = type;
        this.name = name;
    }
}
