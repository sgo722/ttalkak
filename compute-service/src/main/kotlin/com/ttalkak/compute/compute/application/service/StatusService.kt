package com.ttalkak.compute.compute.application.service

import com.ttalkak.compute.common.UseCase
import com.ttalkak.compute.compute.application.port.`in`.LoadStatusUseCase
import com.ttalkak.compute.compute.application.port.out.LoadStatusPort
import com.ttalkak.compute.compute.application.port.out.SaveStatusPort
import com.ttalkak.compute.compute.application.port.`in`.StatusCommand
import com.ttalkak.compute.compute.application.port.`in`.UpdateStatusUseCase
import com.ttalkak.compute.compute.domain.UserStatus
import org.springframework.transaction.annotation.Transactional


@UseCase
@Transactional(readOnly = true)
class StatusService (
    private val loadStatusPort: LoadStatusPort,
    private val saveStatusPort: SaveStatusPort
): LoadStatusUseCase, UpdateStatusUseCase {
    override fun getStatus(userId: Long): UserStatus {
        return loadStatusPort.loadStatus(userId).orElseThrow {
            throw IllegalArgumentException("User not found")
        }
    }

    @Transactional
    override fun upsertStatus(userId: Long, command: StatusCommand) {
        saveStatusPort.saveStatus(userId, command.maxCompute, command.availablePortStart, command.availablePortEnd)
    }
}