package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.ComputeInstance
import com.ttalkak.compute.compute.domain.ComputeRunning
import com.ttalkak.compute.compute.domain.ServiceType
import java.util.*

interface LoadRunningPort {
    fun loadRunning(id: Long, serviceType: ServiceType): Optional<ComputeRunning>

    fun loadRunningByUserId(userId: Long): List<ComputeInstance>
}