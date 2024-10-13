package com.ttalkak.project.project.domain.event;

import lombok.Data;

@Data
public class FaviconEvent {
    private Long projectId;
    private String faviconUrl;
}
