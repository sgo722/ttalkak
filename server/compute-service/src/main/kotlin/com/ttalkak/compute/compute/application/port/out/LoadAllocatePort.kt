package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.adapter.out.cache.entity.ComputeAllocateCache
import java.util.Optional

interface LoadAllocatePort {
    fun findFirst(): ComputeAllocateCache

    /**
     * 큐에 존재하는 첫번째 요소를 삭제한다.
     */
    fun pop(): Optional<ComputeAllocateCache>

    /**
     * 큐의 사이즈를 반환한다.
     *
     * @return 큐 사이즈
     */
    fun size(): Long

    fun findDeploymentIds(): List<Long>
}