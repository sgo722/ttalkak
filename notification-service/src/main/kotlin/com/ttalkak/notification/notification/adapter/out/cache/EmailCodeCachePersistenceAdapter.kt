package com.ttalkak.notification.notification.adapter.out.cache

import com.ttalkak.compute.common.PersistenceAdapter
import com.ttalkak.notification.notification.adapter.out.cache.repository.EmailCodeCacheRepository
import com.ttalkak.notification.notification.application.port.out.LoadCodePort
import com.ttalkak.notification.notification.application.port.out.SaveCodePort
import java.util.Optional

@PersistenceAdapter
class EmailCodeCachePersistenceAdapter (
    private val emailCodeCacheRepository: EmailCodeCacheRepository
): SaveCodePort, LoadCodePort {
    override fun saveCode(email: String, code: String) {
        emailCodeCacheRepository.save(email, code)
    }

    override fun findCode(email: String): Optional<String> {
        return emailCodeCacheRepository.findByEmail(email)
    }
}