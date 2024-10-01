package com.ttalkak.compute.compute.application.port.out

interface SavePortPort {
    /**
     * 포트를 저장한다.
     *
     * @param userId 사용자 아이디
     * @param port 포트 번호
     */
    fun savePort(userId: Long, port: Int)

    /**
     * 포트를 저장한다.
     *
     * @param userId 사용자 아이디
     * @param ports 포트 번호 목록
     */
    fun savePort(userId: Long, ports: List<Int>)
}