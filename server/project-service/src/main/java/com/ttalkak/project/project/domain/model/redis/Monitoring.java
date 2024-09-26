package com.ttalkak.project.project.domain.model.redis;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Builder
@Getter
public class Monitoring {

    private final String answer;
    private final int docCount;
    private final Instant timestamp;
}
