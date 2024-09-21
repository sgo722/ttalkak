package com.ttalkak.notification.notification.application.service

import com.ttalkak.compute.common.UseCase
import com.ttalkak.notification.notification.application.port.`in`.ConfirmCodeUseCase
import com.ttalkak.notification.notification.application.port.`in`.SendCodeUseCase
import com.ttalkak.notification.notification.application.port.out.LoadCodePort
import com.ttalkak.notification.notification.application.port.out.SaveCodePort
import com.ttalkak.notification.notification.application.port.out.VerifyEmailEventPort
import com.ttalkak.notification.notification.domain.EmailConfirmEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

@UseCase
class EmailService (
    private val mailSender: JavaMailSender,
    private val templateEngine: SpringTemplateEngine,
    private val saveCodePort: SaveCodePort,
    private val loadCodePort: LoadCodePort,
    private val verifyEmailEventPort: VerifyEmailEventPort
): ConfirmCodeUseCase, SendCodeUseCase {
    private val log = KotlinLogging.logger {}

    override fun confirmCode(userId: Long, email: String, code: String) {
        val savedCode = loadCodePort.findCode(email).orElseThrow{
            IllegalArgumentException("인증 코드가 존재하지 않습니다.")
        }

        if (savedCode != code) {
            throw IllegalArgumentException("인증 코드가 일치하지 않습니다.")
        }

        EmailConfirmEvent(userId = userId, email = email).let(verifyEmailEventPort::verifyEmail)
    }

    override fun sendCode(email: String, nickname: String) {
        val code = (100000..999999).random().toString()

        saveCodePort.saveCode(email, code)

        val params = mapOf("code" to code, "nickname" to nickname)

        sendEmail(
            "email/verify_member_mail",
            "딸깍 서비스 이메일 인증 코드",
            email,
            params
        )
    }

    @Async
    fun sendEmail(
        templateName: String,
        title: String,
        email: String,
        params: Map<String, String>
    ) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)

        helper.setSubject(title)
        helper.setTo(email)

        val context = Context()
        params.forEach { (key, value) ->
            context.setVariable(key, value)
        }

        val html = templateEngine.process(templateName, context)
        helper.setText(html, true)

        try {
            mailSender.send(message)
            log.info {
                "이메일 인증 코드 메일 발송 성공: $email"
            }
        } catch (e: Exception) {
            log.error(e) {
                "이메일 인증 코드 메일 발송 실패: $email"
            }
        }
    }
}