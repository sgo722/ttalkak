package com.ttalkak.compute.compute.adapter.out.persistence

import com.ttalkak.compute.common.PersistenceAdapter
import com.ttalkak.compute.compute.adapter.out.persistence.entity.StatusEntity
import com.ttalkak.compute.compute.adapter.out.persistence.entity.StatusMapper
import com.ttalkak.compute.compute.adapter.out.persistence.repository.StatusRepository
import com.ttalkak.compute.compute.application.port.out.LoadStatusPort
import com.ttalkak.compute.compute.application.port.out.SaveStatusPort
import com.ttalkak.compute.compute.domain.UserStatus
import java.util.Optional

@PersistenceAdapter
class StatusPersistenceAdapter (
    private val statusRepository: StatusRepository
): LoadStatusPort, SaveStatusPort {
    override fun saveStatus(userId: Long, maxCompute: Int, availablePortStart: Int, availablePortEnd: Int) {
        val status = statusRepository.findByUserId(userId).orElseGet { StatusEntity(userId = userId) }.apply {
            this.maxCompute = maxCompute
            this.availablePortStart = availablePortStart
            this.availablePortEnd = availablePortEnd
        }

        statusRepository.save(status)
    }

    override fun loadStatus(userId: Long): Optional<UserStatus> {
        return statusRepository.findByUserId(userId)
            .map { StatusMapper.toDomain(it) }
    }

    override fun exists(userId: Long): Boolean {
        return statusRepository.findByUserId(userId).isPresent
    }
}