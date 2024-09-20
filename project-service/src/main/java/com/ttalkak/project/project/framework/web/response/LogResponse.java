package com.ttalkak.project.project.framework.web.response;

import com.ttalkak.project.project.domain.model.LogEntryDocument;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LogResponse {

    private String timestamp;
    private String ip;
    private String domain;
    private String path;
    private String method;
    private String status;
    private Double duration;

    @Builder
    public LogResponse(String timestamp, String ip, String domain, String path, String method, String status, Double duration) {
        this.timestamp = timestamp;
        this.ip = ip;
        this.domain = domain;
        this.path = path;
        this.method = method;
        this.status = status;
        this.duration = duration;
    }

    public static LogResponse mapToResponse(LogEntryDocument logDoc) {
        return LogResponse.builder()
                .timestamp(logDoc.getTimestamp())
                .ip(logDoc.getIp())
                .domain(logDoc.getDomain())
                .path(logDoc.getPath())
                .method(logDoc.getMethod())
                .status(logDoc.getStatus())
                .duration(logDoc.getDuration())
                .build();
    }
}
