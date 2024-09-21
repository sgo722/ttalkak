package com.ttalkak.compute.compute.application.port.out

interface CreateConnectPort {
    /**
     * 컴퓨터 연결
     *
     * @param userId 사용자 ID
     * @param sessionId 세션 ID
     * @return Unit
     */
    fun connect(userId: Long, sessionId: String)
}