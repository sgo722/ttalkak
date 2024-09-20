package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.ComputeUser
import java.util.Optional

interface LoadComputePort {
    fun loadCompute(userId: Long): Optional<ComputeUser>
    fun loadAllCompute(): List<ComputeUser>
}