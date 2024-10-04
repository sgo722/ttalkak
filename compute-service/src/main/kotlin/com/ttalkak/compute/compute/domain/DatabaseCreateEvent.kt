package com.ttalkak.compute.compute.domain

data class DatabaseCreateEvent(
    val databaseId: Long,
    val subdomainKey: String,
    val database: Database,
    val port: Int,
)

