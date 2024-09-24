package com.ttalkak.compute.compute.application.port.`in`

import com.ttalkak.compute.compute.domain.AllocateCompute

interface AllocateUseCase {
    fun allocate(command: AllocateCommand): AllocateCompute
}