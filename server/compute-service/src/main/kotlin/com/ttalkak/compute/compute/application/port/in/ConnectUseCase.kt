package com.ttalkak.compute.compute.application.port.`in`

interface ConnectUseCase {
    /**
     * 신규 컴퓨터가 연결되었을 때 호출
     *
     * @param userId 사용자 ID
     * @param sessionId 세션 ID
     * @return Unit
     */
    fun connect(userId: Long, sessionId: String)

    /**
     * 컴퓨터 연결 해제
     *
     * @param sessionId 세션 ID
     * @return Unit
     */
    fun disconnect(sessionId: String)
}