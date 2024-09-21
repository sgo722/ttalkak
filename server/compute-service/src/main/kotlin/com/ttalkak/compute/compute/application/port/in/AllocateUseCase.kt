package com.ttalkak.compute.compute.application.port.`in`

interface AllocateUseCase {
    fun allocate(command: AllocateCommand): Long
}