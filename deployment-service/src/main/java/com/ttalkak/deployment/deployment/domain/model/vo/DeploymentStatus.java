package com.ttalkak.deployment.deployment.domain.model.vo;

import lombok.Getter;

@Getter
public enum DeploymentStatus {
    RUNNING, STOPPED, DELETED, PENDING;

    public static boolean isAlive(DeploymentStatus deploymentStatus) {
        if(deploymentStatus == DELETED){
            return false;
        }
        return true;
    }
}
