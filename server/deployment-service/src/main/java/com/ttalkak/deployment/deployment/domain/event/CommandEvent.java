package com.ttalkak.deployment.deployment.domain.event;

import lombok.Getter;

@Getter
public enum CommandEvent {
    START,
    RESTART,
    STOP,
    DELETE,
    REBUILD
}
