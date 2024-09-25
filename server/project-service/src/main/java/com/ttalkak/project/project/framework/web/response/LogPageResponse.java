package com.ttalkak.project.project.framework.web.response;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class LogPageResponse {
    private List<LogResponse> content;
    private Map<String, Long> methodCounts;
    private Map<String, Long> statusCounts;

    @Builder
    public LogPageResponse(List<LogResponse> content, Map<String, Long> methodCounts, Map<String, Long> statusCounts) {
        this.content = content;
        this.methodCounts = methodCounts;
        this.statusCounts = statusCounts;
    }
}
