package com.ttalkak.deployment.deployment.framework.web.response;

import com.ttalkak.deployment.deployment.domain.model.EnvEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EnvResponse {

    private Long envId;

    private String key;

    private String value;

    @Builder
    private EnvResponse(Long envId, String key, String value) {
        this.envId = envId;
        this.key = key;
        this.value = value;
    }

    public EnvResponse(String key, String value){
        this.key = key;
        this.value = value;
    }

    public static EnvResponse mapToDTO(EnvEntity envEntity){
        return EnvResponse.builder()
                .envId(envEntity.getId())
                .key(envEntity.getKey())
                .value(envEntity.getValue())
                .build();
    }
}
