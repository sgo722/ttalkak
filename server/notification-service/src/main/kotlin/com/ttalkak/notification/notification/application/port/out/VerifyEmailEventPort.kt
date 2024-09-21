package com.ttalkak.notification.notification.application.port.out

import com.ttalkak.notification.notification.domain.EmailConfirmEvent

interface VerifyEmailEventPort {
    /**
     * 인증이 완료된 경우 유저 서비스에 이메일 인증 요청을 보낸다.
     *
     * @param event 이메일 인증 이벤트
     */
    fun verifyEmail(event: EmailConfirmEvent)
}