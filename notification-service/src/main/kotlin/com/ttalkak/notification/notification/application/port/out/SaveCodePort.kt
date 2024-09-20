package com.ttalkak.notification.notification.application.port.out

interface SaveCodePort {
    /**
     * 인증 코드를 저장한다.
     *
     * @param email 이메일
     * @param code 인증 코드
    */
    fun saveCode(email: String, code: String)
}