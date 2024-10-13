package com.ttalkak.project.project.framework.web.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DomainNameResponse {
    private String message;
    private boolean canMake;

    @Builder
    public DomainNameResponse(boolean canMake, String message) {
        this.canMake = canMake;
        this.message = message;
    }
}
