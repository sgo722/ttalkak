package com.ttalkak.deployment.deployment.framework.web.request;

import com.ttalkak.deployment.deployment.domain.event.CommandEvent;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DatabaseCommandStatusRequest {

    private Long databaseId;

    @Enumerated(EnumType.STRING)
    private CommandEvent command;

    @Builder
    public DatabaseCommandStatusRequest(Long databaseId, CommandEvent command) {
        this.databaseId = databaseId;
        this.command = command;
    }
}