package com.ttalkak.compute.compute.application.port.out

interface RedisLockPort {
    /**
     * 레디스에서 키를 이용하여 락을 건다.
     *
     * @param key
     * @param expire
     * @return 락 걸기 성공 여부
     */
    fun lock(key: String, expire: Long): Boolean

    /**
     * 레디스에서 키를 이용하여 락을 해제한다.
     *
     * @param key
     * @return 락 해제 성공 여부
     */
    fun unlock(key: String): Boolean

    /**
     * 레디스에서 키를 이용하여 락이 걸려있는지 확인한다.
     *
     * @param key
     * @return 락이 걸려있는지 여부
     */
    fun isLockHold(key: String): Boolean
}