package com.ttalkak.compute.compute.domain

data class AllocateCompute(
    val userId: Long,
    val ports: List<Int>
)
