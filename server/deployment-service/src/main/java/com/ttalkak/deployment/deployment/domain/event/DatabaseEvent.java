package com.ttalkak.deployment.deployment.domain.event;

import com.ttalkak.deployment.deployment.domain.model.vo.DatabaseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseEvent {

    private DatabaseType databaseType;

    private String name;

    private String dbName;

    private String username;

    private String password;
}
