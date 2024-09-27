package com.ttalkak.compute.compute.application.port.out

interface LoadConnectPort {
    /**
     * 연결된 사용자 조회
     *
     * @return 연결된 사용자 ID 목록
     */
    fun loadConnectUser(): List<Long>

    /**
     * 연결된 사용자 조회
     *
     * @return 연결된 사용자 ID 목록
     */
}