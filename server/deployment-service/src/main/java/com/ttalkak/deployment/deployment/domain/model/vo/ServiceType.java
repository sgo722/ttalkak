package com.ttalkak.deployment.deployment.domain.model.vo;

import lombok.Getter;

@Getter
public enum ServiceType {
    FRONTEND, BACKEND;

    public static boolean isBackendType(String serviceType){
        if(ServiceType.valueOf(serviceType).equals(BACKEND)){
            return true;
        }
        return false;
    }

    public static boolean isFrontendType(String serviceType){
        if(ServiceType.valueOf(serviceType).equals(FRONTEND)){
            return true;
        }
        return false;
    }
}
