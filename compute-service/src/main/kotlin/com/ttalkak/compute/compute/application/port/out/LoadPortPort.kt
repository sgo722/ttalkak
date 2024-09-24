package com.ttalkak.compute.compute.application.port.out

interface LoadPortPort {
    /**
     * 현재 특정한 사용자가 사용하는 포트를 전부 조회한다.
     *
     * @param userId 사용자 아이디
     *
     * @return 사용중인 포트 리스트
     */
    fun loadPorts(userId: Long): List<Int>
}