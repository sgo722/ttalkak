package com.ttalkak.notification.notification.application.port.out

import java.util.*

interface LoadCodePort {
    /**
     * 인증 코드를 찾는다.
     *
     * @param email 이메일
     * @return Optional<String>
     */
    fun findCode(email: String): Optional<String>
}