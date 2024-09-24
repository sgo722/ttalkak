package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.ComputerType

interface SaveComputePort {
    /**
     * 컴퓨터 정보 저장
     *
     * @param userId 사용자 ID
     * @param computeType 컴퓨터 타입
     * @param maxMemory 최대 메모리
     * @return Unit
     */
    fun saveCompute(
        userId: Long,
        computeType: ComputerType,
        usedCompute: Int,
        usedMemory: Int,
        usedCPU: Double
    )

    /**
     * 컴퓨터 정보 삭제
     *
     * @param userId 사용자 ID
     * @return Unit
     */
    fun deleteCompute(userId: Long)
}