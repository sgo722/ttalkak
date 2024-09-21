package com.ttalkak.compute.compute.adapter.out.cache

import com.ttalkak.compute.common.PersistenceAdapter
import com.ttalkak.compute.compute.adapter.out.cache.entity.ComputeUserCache
import com.ttalkak.compute.compute.adapter.out.cache.entity.RunningCache
import com.ttalkak.compute.compute.adapter.out.cache.repository.ComputeUserCacheRepository
import com.ttalkak.compute.compute.adapter.out.cache.repository.RunningCacheRepository
import com.ttalkak.compute.compute.application.port.out.LoadComputePort
import com.ttalkak.compute.compute.application.port.out.LoadRunningPort
import com.ttalkak.compute.compute.application.port.out.SaveComputePort
import com.ttalkak.compute.compute.application.port.out.SaveRunningPort
import com.ttalkak.compute.compute.domain.ComputeRunning
import com.ttalkak.compute.compute.domain.ComputeUser
import com.ttalkak.compute.compute.domain.ComputerType
import com.ttalkak.compute.compute.domain.RunningStatus
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.*

@PersistenceAdapter
class ComputeCachePersistenceAdapter(
    private val computeUserCacheRepository: ComputeUserCacheRepository,
    private val runningCacheRepository: RunningCacheRepository
): SaveComputePort, LoadComputePort, SaveRunningPort, LoadRunningPort {
    private val log = KotlinLogging.logger {  }

    override fun saveCompute(
        userId: Long,
        computeType: ComputerType,
        usedCompute: Int,
        usedMemory: Int,
        usedCPU: Double
    ) {
        val compute = ComputeUserCache(
            userId = userId,
            usedCompute = 0,
            computeType = computeType,
            usedMemory = usedMemory,
            usedCPU = usedCPU
        )

        computeUserCacheRepository.save(compute)
    }

    override fun deleteCompute(userId: Long) {
        computeUserCacheRepository.delete(userId)
    }

    override fun loadCompute(userId: Long): Optional<ComputeUser> {
        // TODO: 해당 부분 수정 필요함.
        return computeUserCacheRepository.findById(userId).map {
                ComputeUser(
                    userId = it.userId,
                    computeType = it.computeType,
                    remainCompute = it.usedCompute,
                    remainMemory = it.usedMemory,
                    remainCPU = it.usedCPU
                )
            }
    }

    override fun loadAllCompute(): List<ComputeUser> {
        return computeUserCacheRepository.findAll().map {
            ComputeUser(
                userId = it.userId,
                computeType = it.computeType,
                remainCompute = it.usedCompute,
                remainMemory = it.usedMemory,
                remainCPU = it.usedCPU
            )
        }
    }

    override fun saveRunning(userId: Long, deploymentId: Long, status: RunningStatus, message: String?) {
        runningCacheRepository.save(deploymentId = deploymentId, RunningCache(
            userId = userId,
            status = status,
            message = message ?: ""
        ))
    }

    override fun loadRunning(deploymentId: Long): ComputeRunning {
        log.debug {
            "deploymentId: $deploymentId, runningCacheRepository.findById(deploymentId): ${runningCacheRepository.findById(deploymentId)}"
        }

        return runningCacheRepository.findById(deploymentId).map {
            ComputeRunning(
                userId = it.userId,
                status = it.status,
                message = it.message
            )
        }.orElseThrow {
            RuntimeException("현재 실행중인 인스턴스가 없습니다.")
        }
    }
}