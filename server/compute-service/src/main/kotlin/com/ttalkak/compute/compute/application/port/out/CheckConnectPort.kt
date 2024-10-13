
package com.ttalkak.compute.compute.application.port.out

interface CheckConnectPort {
    /**
     * 특정한 사용자가 현재 연결되어있는지 확인
     *
     * @param userId 사용자 ID
     * @return Boolean
     */
    fun isConnected(userId: Long): Boolean
}