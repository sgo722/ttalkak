package com.ttalkak.compute.compute.application.port.`in`

interface UpdateStatusUseCase {
    fun upsertStatus(userId: Long, command: StatusCommand)
}