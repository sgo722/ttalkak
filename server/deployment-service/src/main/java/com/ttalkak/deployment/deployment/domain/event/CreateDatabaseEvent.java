package com.ttalkak.deployment.deployment.domain.event;


import com.ttalkak.deployment.deployment.framework.web.request.DatabaseCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.dialect.Database;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDatabaseEvent implements Serializable {

    private Long databaseId;

    private String subdomainKey;

    private DatabaseEvent database;

    private int port;
}
