package com.ttalkak.compute.compute.application.port.out

interface RemoveConnectPort {
    /**
     * 컴퓨터 연결 해제
     *
     * @param sessionId 세션 ID
     * @return Unit
     */
    fun disconnect(sessionId: String): Long

    /**
     * 컴퓨터 연결 해제
     *
     * @param userId 사용자 ID
     * @return Unit
     */
    fun disconnect(userId: Long)
}