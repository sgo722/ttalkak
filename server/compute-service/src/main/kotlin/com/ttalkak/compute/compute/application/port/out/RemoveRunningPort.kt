package com.ttalkak.compute.compute.application.port.out

interface RemoveRunningPort {
    /**
     * 특정한 사용자의 실행중인 컴퓨터를 삭제한다.
     *
     * @param deploymentId 배포 아이디
     */
    fun removeRunningByDeploymentId(deploymentId: Long)

    /**
     * 특정한 사용자의 모든 실행중인 컴퓨터를 삭제한다.
     *
     * @param userId 사용자 아이디
     */
    fun removeRunningByUserId(userId: Long)
}