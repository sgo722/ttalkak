package com.ttalkak.deployment.deployment.application.outputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.deployment.domain.event.CreateDatabaseEvent;
import com.ttalkak.deployment.deployment.domain.event.CreateInstanceEvent;

public interface EventOutputPort {
    void occurCreateInstance(CreateInstanceEvent createInstanceEvent) throws JsonProcessingException;
    void occurRebuildInstance(CreateInstanceEvent createInstanceEvent) throws JsonProcessingException;
    void occurCreateDatabase(CreateDatabaseEvent createDatabaseEvent) throws JsonProcessingException;
}
