package com.ttalkak.notification.notification.adapter.`in`.web

import com.ttalkak.compute.common.WebAdapter
import com.ttalkak.notification.notification.adapter.`in`.web.request.EmailCodeRequest
import com.ttalkak.notification.notification.adapter.`in`.web.request.EmailConfirmRequest
import com.ttalkak.notification.notification.application.port.`in`.ConfirmCodeUseCase
import com.ttalkak.notification.notification.application.port.`in`.SendCodeUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@WebAdapter
@RestController
@RequestMapping("/v1/notification/email")
class EmailController (
    private val sendCodeUseCase: SendCodeUseCase,
    private val confirmCodeUseCase: ConfirmCodeUseCase
) {
    @PostMapping("/code")
    fun sendEmailCode(
        @RequestBody request: EmailCodeRequest,
    ) {
        sendCodeUseCase.sendCode(request.email, request.nickname)
    }

    @PostMapping("/confirm")
    fun confirmEmailCode(
        @RequestBody request: EmailConfirmRequest,
    ) {
        return confirmCodeUseCase.confirmCode(request.userId, request.email, request.code)
    }
}