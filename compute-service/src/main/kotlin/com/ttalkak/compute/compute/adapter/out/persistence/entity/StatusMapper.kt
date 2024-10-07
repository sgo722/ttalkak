package com.ttalkak.compute.compute.adapter.out.persistence.entity

import com.ttalkak.compute.compute.application.port.`in`.StatusCommand
import com.ttalkak.compute.compute.domain.UserStatus

class StatusMapper {
    companion object {
        fun toStatus(userId: Long, statusCommand: StatusCommand): StatusEntity {
            return StatusEntity(
                userId = userId,
                maxCompute = statusCommand.maxCompute,
                availablePortStart = statusCommand.availablePortStart,
                availablePortEnd = statusCommand.availablePortEnd
            )
        }

        fun toDomain(statusEntity: StatusEntity): UserStatus {
            return UserStatus(
                userId = statusEntity.userId,
                address = statusEntity.address,
                maxCompute = statusEntity.maxCompute,
                availablePortStart = statusEntity.availablePortStart,
                availablePortEnd = statusEntity.availablePortEnd,
                maxMemory = statusEntity.maxMemory,
                maxCPU = statusEntity.maxCPU,
            )
        }
    }
}