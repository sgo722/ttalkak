package com.ttalkak.compute.compute.adapter.out.persistence.repository

import com.ttalkak.compute.compute.adapter.out.persistence.entity.StatusEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface StatusRepository: JpaRepository<StatusEntity, Long> {
    fun findByUserId(userId: Long): Optional<StatusEntity>
}