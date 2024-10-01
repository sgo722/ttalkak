package com.ttalkak.compute.compute.domain

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

    val weight = COMPUTE_WEIGHT * (remainCompute / maxCompute) +
            MEMORY_WEIGHT * (remainMemory / maxMemory) +
            CPU_WEIGHT * (remainCPU / maxCPU)

    fun isAvailable(
        compute: Int,
        memory: Double,
        cpu: Double
    ): Boolean {
        return remainCompute >= compute &&
                remainMemory >= memory &&
                remainCPU >= cpu
    }
}
