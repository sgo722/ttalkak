package com.ttalkak.deployment.deployment.domain.event;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDatabaseEvent implements Serializable {

    private Long id;

    private String name;

    private String type;

    private String username;

    private String password;

    private int port;
}
