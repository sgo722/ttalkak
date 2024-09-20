package com.ttalkak.compute.compute.adapter.`in`.socket.request

import com.ttalkak.compute.compute.domain.RunningStatus


data class ComputeRunningRequest(
    val status: RunningStatus,
    val message: String = ""
)
