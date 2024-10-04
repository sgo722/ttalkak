package com.ttalkak.compute.compute.domain

data class Database(
    val databaseType: DatabaseType,
    val name: String,
    val username: String,
    val password: String,
)
