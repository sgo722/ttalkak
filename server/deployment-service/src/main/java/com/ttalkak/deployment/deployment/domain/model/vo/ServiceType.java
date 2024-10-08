package com.ttalkak.deployment.deployment.domain.model.vo;

import lombok.Getter;

@Getter
public enum ServiceType {
    FRONTEND, BACKEND, DATABASE;

    public static boolean isBackendType(ServiceType serviceType){
        if(serviceType.equals(BACKEND)){
            return true;
        }
        return false;
    }

    public static boolean isFrontendType(ServiceType serviceType){
        if(serviceType.equals(FRONTEND)){
            return true;
        }
        return false;
    }

    public static boolean isDatabase(ServiceType serviceType){
        if(serviceType.equals(DATABASE)){
            return true;
        }
        return false;
    }
}
