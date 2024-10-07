package com.ttalkak.compute.compute.adapter.out.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.ColumnDefault

@Entity
data class StatusEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "max_compute", nullable = false)
    @ColumnDefault("0")
    var maxCompute: Int = 0,

    @Column(name = "max_memory", nullable = false)
    @ColumnDefault("0")
    var maxMemory: Int = 0,

    @Column(name = "max_cpu", nullable = false)
    @ColumnDefault("0.0")
    var maxCPU: Double = 0.0,

    @Column(name = "available_port_start", nullable = false)
    @ColumnDefault("10000")
    var availablePortStart: Int = 10000,

    @Column(name = "available_port_end", nullable = false)
    @ColumnDefault("20000")
    var availablePortEnd: Int = 20000,

    @Column(name = "address")
    var address: String = ""
)
