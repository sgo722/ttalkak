package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.UserStatus
import java.util.Optional

interface LoadStatusPort {
    fun loadStatus(userId: Long): Optional<UserStatus>
    fun exists(userId: Long): Boolean
}