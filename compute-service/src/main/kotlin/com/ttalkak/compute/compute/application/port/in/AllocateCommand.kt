package com.ttalkak.compute.compute.application.port.`in`

data class AllocateCommand(
    val computeCount: Int,
    val useMemory: Int,
    val usePorts: List<Int>
)
