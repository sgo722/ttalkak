package com.ttalkak.compute.compute.application.port.out

interface RemoveDeploymentStatusPort {
    /**
     * 배포 상태를 삭제한다.
     *
     * @param deploymentId 배포 아이디
     */
    fun removeDeploymentStatus(deploymentId: Long)

    /**
     * 특정한 사용자의 배포 상태를 삭제한다.
     *
     * @param userId 사용자 아이디
     */
    fun removeDeploymentStatusByUserId(userId: Long)
}