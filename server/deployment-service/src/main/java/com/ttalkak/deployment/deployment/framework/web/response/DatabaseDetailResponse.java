package com.ttalkak.deployment.deployment.framework.web.response;


import com.ttalkak.deployment.deployment.domain.model.DatabaseEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DatabaseDetailResponse {

    private Long id;

    private String name;

    private String type;

    private String username;

    private String password;

    private int port;

    private Status status;

    private String statusMessage;


    @Builder
    private DatabaseDetailResponse(Long id, String name, String type, String username, String password, int port, Status status, String statusMessage) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.username = username;
        this.password = password;
        this.port = port;
        this.status = status;
        this.statusMessage = statusMessage;
    }

    public static DatabaseDetailResponse mapToDTO(DatabaseEntity databaseEntity){
        return DatabaseDetailResponse.builder()
                .id(databaseEntity.getId())
                .name(databaseEntity.getName())
                .type(databaseEntity.getDatabaseType().toString())
                .username(databaseEntity.getUsername())
                .password(databaseEntity.getPassword())
                .port(databaseEntity.getPort())
                .status(databaseEntity.getStatus())
                .statusMessage(databaseEntity.getStatusMessage())
                .build();
    }
}

