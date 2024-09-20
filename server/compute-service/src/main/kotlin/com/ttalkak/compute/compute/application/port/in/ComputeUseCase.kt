package com.ttalkak.compute.compute.application.port.`in`

interface ComputeUseCase {
    /**
     * 신규 컴퓨터가 연결되었을 때 호출
     *
     * @param command 연결 정보
     * @return Unit
     */
    fun connect(command: ConnectCommand)

    /**
     * 컴퓨터가 연결 해제되었을 때 호출
     *
     * @param userId 사용자 ID
     * @return Unit
     */
    fun disconnect(userId: Long)
}