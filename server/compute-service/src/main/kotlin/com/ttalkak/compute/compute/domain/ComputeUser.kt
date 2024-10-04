package com.ttalkak.compute.compute.domain

import io.github.oshai.kotlinlogging.KotlinLogging

data class ComputeUser(
    val userId: Long,
    val computeType: ComputerType,
    val remainCompute: Int,
    val maxCompute: Int,
    val remainMemory: Double,
    val maxMemory: Double,
    val remainCPU: Double,
    val maxCPU: Double
) {
    companion object {
        const val COMPUTE_WEIGHT = 0.5
        const val MEMORY_WEIGHT = 0.3
        const val CPU_WEIGHT = 0.2
    }

    private val log = KotlinLogging.logger {  }

    val weight = COMPUTE_WEIGHT * (remainCompute / maxCompute) +
            MEMORY_WEIGHT * (remainMemory / maxMemory) +
            CPU_WEIGHT * (remainCPU / maxCPU)

    fun isAvailable(
        memory: Double,
        cpu: Double
    ): Boolean {
        log.info { "userId: $userId, remainCompute: $remainCompute, maxCompute: $maxCompute, remainMemory: $remainMemory, maxMemory: $maxMemory, remainCPU: $remainCPU, maxCPU: $maxCPU, memory: $memory, cpu: $cpu" }
        return remainCompute >= 1 &&
                remainMemory >= memory &&
                remainCPU >= cpu
    }
}
