package com.ttalkak.compute.compute.application.port.out

import com.ttalkak.compute.compute.domain.ServiceType

interface RemoveRunningPort {
    /**
     * 특정한 사용자의 실행중인 컴퓨터를 삭제한다.
     *
     * @param id 컴퓨터 아이디
     * @param serviceType 서비스 타입
     */
    fun removeRunningByDeploymentId(id: Long, serviceType: ServiceType)

    /**
     * 특정한 사용자의 모든 실행중인 컴퓨터를 삭제한다.
     *
     * @param userId 사용자 아이디
     */
    fun removeRunningByUserId(userId: Long)
}