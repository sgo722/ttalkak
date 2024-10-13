package com.ttalkak.notification

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import java.util.*

@SpringBootApplication
@EnableDiscoveryClient
class NotificationServiceApplication

fun main(args: Array<String>) {
    @PostConstruct
    fun init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
    }

    runApplication<NotificationServiceApplication>(*args)
}
