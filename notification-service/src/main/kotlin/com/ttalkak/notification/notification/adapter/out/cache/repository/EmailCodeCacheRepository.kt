package com.ttalkak.notification.notification.adapter.out.cache.repository

import jakarta.annotation.Resource
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.concurrent.TimeUnit

@Repository
class EmailCodeCacheRepository {
    companion object {
        private const val EXPIRE_MINUTES = 5L
        private const val EMAIL_CONFIRM_CACHE_KEY = "email-confirm"
    }
    @Resource(name = "redisTemplate")
    private lateinit var valueOperations: ValueOperations<String, String>

    fun save(email: String, code: String) {
        if (isSent(email)) {
            throw IllegalStateException("이미 인증 코드가 발송되었습니다.")
        }
        valueOperations.set(emailConfirmKey(email), code, EXPIRE_MINUTES, TimeUnit.MINUTES)
    }

    fun findByEmail(email: String): Optional<String> {
        return Optional.ofNullable(valueOperations.get(emailConfirmKey(email)))
    }

    private fun isSent(email: String) = valueOperations.get(emailConfirmKey(email)) != null

    private fun emailConfirmKey(email: String) = "$EMAIL_CONFIRM_CACHE_KEY:$email"
}