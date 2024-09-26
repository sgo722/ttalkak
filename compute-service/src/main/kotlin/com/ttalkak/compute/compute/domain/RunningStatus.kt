package com.ttalkak.compute.compute.domain

enum class RunningStatus {
    RUNNING,
    STOPPED,
    DELETED,
    PENDING,
    CLOUD_MANIPULATE,
    DOCKER_FILE_ERROR,
    ALLOCATE_ERROR;
}
