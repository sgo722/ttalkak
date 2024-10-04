package com.ttalkak.deployment.deployment.framework.web.response;


import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.domain.model.DeploymentEntity;
import com.ttalkak.deployment.deployment.domain.model.HostingEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.DatabaseType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DatabaseResponse {

    private Long id;

    private String name;

    private String type;

    private String username;

    private String password;

    private int port;


    @Builder
    private DatabaseResponse(Long id, String name, String type, String username, String password, int port) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public static DatabaseResponse mapToDTO(DatabaseEntity databaseEntity){
        return DatabaseResponse.builder()
                .id(databaseEntity.getId())
                .name(databaseEntity.getName())
                .type(databaseEntity.getDatabaseType().toString())
                .username(databaseEntity.getUsername())
                .password(databaseEntity.getPassword())
                .build();
    }
}

