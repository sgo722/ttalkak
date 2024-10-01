package com.ttalkak.compute.compute.adapter.out.cache

import com.ttalkak.compute.common.PersistenceAdapter
import com.ttalkak.compute.compute.adapter.out.cache.entity.ComputeUserCache
import com.ttalkak.compute.compute.adapter.out.cache.entity.RunningCache
import com.ttalkak.compute.compute.adapter.out.cache.repository.ComputePortCacheRepository
import com.ttalkak.compute.compute.adapter.out.cache.repository.ComputeUserCacheRepository
import com.ttalkak.compute.compute.adapter.out.cache.repository.RunningCacheRepository
import com.ttalkak.compute.compute.adapter.out.persistence.repository.StatusRepository
import com.ttalkak.compute.compute.application.port.out.*
import com.ttalkak.compute.compute.domain.ComputeRunning
import com.ttalkak.compute.compute.domain.ComputeUser
import com.ttalkak.compute.compute.domain.ComputerType
import com.ttalkak.compute.compute.domain.RunningStatus
import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.*
import kotlin.math.max
import kotlin.math.min

@PersistenceAdapter
class ComputeCachePersistenceAdapter(
    private val computeUserCacheRepository: ComputeUserCacheRepository,
    private val runningCacheRepository: RunningCacheRepository,
    private val computePortCacheRepository: ComputePortCacheRepository,
    private val statusRepository: StatusRepository
): SaveComputePort, LoadComputePort, SaveRunningPort, LoadRunningPort, LoadPortPort, RemovePortPort, RemoveRunningPort, SavePortPort {
    private val log = KotlinLogging.logger {  }

    override fun saveCompute(
        userId: Long,
        computeType: ComputerType,
        usedCompute: Int,
        usedMemory: Double,
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
        val status = statusRepository.findByUserId(userId).orElseThrow {
            RuntimeException("상태가 존재하지 않습니다.")
        }

        return computeUserCacheRepository.findById(userId).map {
            ComputeUser(
                userId = it.userId,
                computeType = it.computeType,
                maxCompute = status.maxCompute,
                maxMemory = status.maxMemory.toDouble(),
                maxCPU = status.maxCPU,
                remainCompute = status.maxCompute - it.usedCompute,
                remainMemory = status.maxMemory - it.usedMemory,
                remainCPU = status.maxCPU - it.usedCPU
            )
        }
    }

    override fun loadAllCompute(): List<ComputeUser> {
        return computeUserCacheRepository.findAll().map {
            val status = statusRepository.findByUserId(it.userId).orElseThrow {
                RuntimeException("상태가 존재하지 않습니다.")
            }

            ComputeUser(
                userId = it.userId,
                computeType = it.computeType,
                maxCompute = status.maxCompute,
                maxMemory = status.maxMemory.toDouble(),
                maxCPU = status.maxCPU,
                remainCompute = status.maxCompute - it.usedCompute,
                remainMemory = status.maxMemory - it.usedMemory,
                remainCPU = status.maxCPU - it.usedCPU
            )
        }
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

    override fun saveRunning(userId: Long, deploymentId: Long, port: Int, status: RunningStatus, message: String?) {
        if (status == RunningStatus.DELETED) {
            runningCacheRepository.delete(deploymentId)
            computePortCacheRepository.delete(userId, port)
            return
        }

        runningCacheRepository.save(deploymentId = deploymentId, RunningCache(
            userId = userId,
            status = status,
            message = message ?: ""
        )).also {
            computePortCacheRepository.save(
                userId = userId,
                port = port
            )
        }
    }

    override fun loadPorts(userId: Long): List<Int> {
        return computePortCacheRepository.findAll(userId)
    }

    override fun removePort(userId: Long, port: Int) {
        computePortCacheRepository.delete(userId, port)
    }

    override fun removePort(userId: Long) {
        computePortCacheRepository.delete(userId)
    }

    override fun removeRunningByDeploymentId(deploymentId: Long) {
        runningCacheRepository.delete(deploymentId)
    }

    override fun removeRunningByUserId(userId: Long) {
        runningCacheRepository.deleteByUserId(userId)
    }

    override fun savePort(userId: Long, port: Int) {
        computePortCacheRepository.save(userId, port)
    }

    override fun savePort(userId: Long, ports: List<Int>) {
        computePortCacheRepository.save(userId, ports)
    }
}