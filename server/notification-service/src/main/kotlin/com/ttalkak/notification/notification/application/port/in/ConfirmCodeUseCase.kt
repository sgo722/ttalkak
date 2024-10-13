package com.ttalkak.notification.notification.application.port.`in`

interface ConfirmCodeUseCase {
    /**
     * 인증 코드를 확인한다.
     *
     * @param userId 사용자 ID
     * @param email 이메일
     * @param code 인증 코드
     */
    fun confirmCode(userId: Long, email: String, code: String)
}