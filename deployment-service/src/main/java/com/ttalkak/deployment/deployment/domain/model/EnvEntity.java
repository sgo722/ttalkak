package com.ttalkak.deployment.deployment.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class EnvEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String key;

    private String value;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deploymentEntity", nullable = false)
    private DeploymentEntity deploymentEntity;

    @Builder
    private EnvEntity(String key, String value, DeploymentEntity deploymentEntity) {
        this.key = key;
        this.value = value;
        this.deploymentEntity = deploymentEntity;
    }

    public static EnvEntity create(String key, String value, DeploymentEntity deploymentEntity) {
        return EnvEntity.builder()
                .key(key)
                .value(value)
                .deploymentEntity(deploymentEntity)
                .build();
    }
}
