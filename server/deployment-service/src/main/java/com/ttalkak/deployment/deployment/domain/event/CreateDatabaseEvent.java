package com.ttalkak.deployment.deployment.domain.event;


import com.ttalkak.deployment.deployment.framework.web.request.DatabaseCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.dialect.Database;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class CreateDatabaseEvent implements Serializable {

    private Long senderId;

    private Long databaseId;

    private String subdomainKey;

    private DatabaseEvent database;

    private int port;

    public CreateDatabaseEvent(Long senderId, Long databaseId, String subdomainKey, DatabaseEvent database, int port) {
        this.senderId = senderId;
        this.databaseId = databaseId;
        this.subdomainKey = subdomainKey;
        this.database = database;
        this.port = port;
    }
}
