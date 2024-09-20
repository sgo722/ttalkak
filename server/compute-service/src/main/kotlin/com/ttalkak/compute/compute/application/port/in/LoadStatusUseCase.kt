package com.ttalkak.compute.compute.application.port.`in`

import com.ttalkak.compute.compute.domain.UserStatus

interface LoadStatusUseCase {
    fun getStatus(userId: Long): UserStatus
}