package com.ttalkak.compute.compute.application.port.`in`

interface DisconnectScheduleUseCase {
    /**
     * 스케쥴링을 사용해서 연결이 끊긴 컴퓨터의 정보를 제거해준다.
     *
     */
    fun disconnectSchedule()
}