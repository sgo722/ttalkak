package com.ttalkak.compute.compute.domain

data class DatabaseContainer(
    val name: String,
    val tag: String,
    val envs: List<Environment>,
)
