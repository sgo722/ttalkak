package com.ttalkak.deployment.deployment.domain.event;

import lombok.Data;

@Data
public class UpdateFaviconEvent {
    private Long projectId;
    private String faviconUrl;
}
