package com.ttalkak.compute.compute.application.port.`in`

interface UpsertRunningUseCase {
    fun upsertRunning(userId: Long, runningCommand: RunningCommand)
}