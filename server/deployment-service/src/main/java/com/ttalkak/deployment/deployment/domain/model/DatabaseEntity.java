package com.ttalkak.deployment.deployment.domain.model;

import com.ttalkak.deployment.common.BaseEntity;
import com.ttalkak.deployment.deployment.domain.model.vo.DatabaseEditor;
import com.ttalkak.deployment.deployment.domain.model.vo.Status;
import jakarta.persistence.*;
import com.ttalkak.deployment.deployment.domain.model.vo.DatabaseType;
import lombok.*;

import java.util.UUID;

import static com.ttalkak.deployment.deployment.domain.model.vo.Status.PENDING;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DatabaseEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DatabaseType databaseType;

    private String username;

    private String password;

    @Setter
    private int port;


    @Setter
    @Column(name = "database_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Setter
    private String statusMessage;

    @Builder
    private DatabaseEntity(Long userId, String name, DatabaseType databaseType) {
        this.userId = userId;
        this.name = name;
        this.databaseType = databaseType;
        this.status = PENDING;
        this.statusMessage = PENDING.toString().toLowerCase();
        this.username = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        this.password = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        this.port = -1;
    }

    public static DatabaseEntity createDatabase(Long userId,
                                                String name,
                                                DatabaseType databaseType) {
        return DatabaseEntity.builder()
                .userId(userId)
                .name(name)
                .databaseType(databaseType)
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
