package com.ttalkak.compute.compute.domain

data class Database(
    val databaseId: Long,
    val databaseType: DatabaseType,
    val username: String,
    val password: String,
    val port: Int,
)
