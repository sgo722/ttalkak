package com.ttalkak.deployment.deployment.domain.model.vo;

import lombok.Getter;

@Getter
public enum DeploymentStatus {
    RUNNING, STOPPED, DELETED, PENDING, WAITING, ERROR;

//    CLOUD_MANIPULATE, DOCKER_FILE_ERROR, ALLOCATE_ERROR;


    public static boolean isAlive(DeploymentStatus deploymentStatus) {
        return deploymentStatus != DELETED;
    }
}