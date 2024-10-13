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
public class DatabasePreviewResponse {

    private Long id;

    private String name;

    private String type;

    private int port;

    private Status status;

    private String statusMessage;


    @Builder
    private DatabasePreviewResponse(Long id, String name, String type, int port, Status status, String statusMessage) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.port = port;
        this.status = status;
        this.statusMessage = statusMessage;
    }

    public static DatabasePreviewResponse mapToDTO(DatabaseEntity databaseEntity){
        return DatabasePreviewResponse.builder()
                .id(databaseEntity.getId())
                .name(databaseEntity.getName())
                .type(databaseEntity.getDatabaseType().toString())
                .port(databaseEntity.getPort())
                .status(databaseEntity.getStatus())
                .statusMessage(databaseEntity.getStatusMessage())
                .build();
    }
}

