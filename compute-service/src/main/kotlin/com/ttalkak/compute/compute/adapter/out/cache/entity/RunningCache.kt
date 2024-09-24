package com.ttalkak.compute.compute.adapter.out.cache.entity

import com.ttalkak.compute.compute.domain.RunningStatus

data class RunningCache(
    val userId: Long,
    val status: RunningStatus,
    val message: String
)
