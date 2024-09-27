package com.ttalkak.deployment.deployment.domain.model;

import com.ttalkak.deployment.deployment.domain.model.vo.DatabaseEditor;
import jakarta.persistence.*;
import com.ttalkak.deployment.deployment.domain.model.vo.DatabaseType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DatabaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deploymentEntity", nullable = false)
    private DeploymentEntity deploymentEntity;

    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DatabaseType databaseType;

    private String username;

    private String password;

    private int port;

    @Builder
    private DatabaseEntity(DeploymentEntity deploymentEntity, String name, DatabaseType databaseType, String username, String password, int port) {
        this.deploymentEntity = deploymentEntity;
        this.name = name;
        this.databaseType = databaseType;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public static DatabaseEntity createDatabase(DeploymentEntity deploymentEntity,
                                                int port,
                                                String name,
                                                DatabaseType databaseType,
                                                String username,
                                                String password) {
        return DatabaseEntity.builder()
                .deploymentEntity(deploymentEntity)
                .port(port)
                .name(name)
                .databaseType(databaseType)
                .username(username)
                .password(password)
                .build();

    }

    public DatabaseEditor.DatabaseEditorBuilder toEditor() {
        return DatabaseEditor.builder()
                .username(this.username)
                .password(this.password)
                .port(this.port);
    }

    public void edit(DatabaseEditor databaseEditor) {
        this.username = databaseEditor.getUsername();
        this.password = databaseEditor.getPassword();
        this.port = databaseEditor.getPort();
    }
}
