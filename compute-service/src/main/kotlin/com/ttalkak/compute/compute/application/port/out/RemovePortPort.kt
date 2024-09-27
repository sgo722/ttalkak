package com.ttalkak.compute.compute.application.port.out

interface RemovePortPort {
    /**
     * 특정한 사용자의 포트를 삭제한다.
     *
     * @param userId 사용자 아이디
     * @param port 포트 번호
     *
     */
    fun removePort(userId: Long, port: Int)

    /**
     * 특정한 사용자의 모든 포트를 삭제한다.
     *
     * @param userId 사용자 아이디
     *
     */
    fun removePort(userId: Long)
}