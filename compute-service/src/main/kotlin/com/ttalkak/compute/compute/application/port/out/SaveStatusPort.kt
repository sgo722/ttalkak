package com.ttalkak.compute.compute.application.port.out

interface SaveStatusPort {
    /**
     * 컴퓨터 리소스 사용을 저장
     *
     * @param userId 사용자 아이디
     * @param maxCompute 최대 컴퓨터 사용량
     * @param availablePortStart 사용 가능한 포트 시작
     * @param availablePortEnd 사용 가능한 포트 끝
     */
    fun saveStatus(userId: Long, maxCompute: Int, availablePortStart: Int, availablePortEnd: Int)
}