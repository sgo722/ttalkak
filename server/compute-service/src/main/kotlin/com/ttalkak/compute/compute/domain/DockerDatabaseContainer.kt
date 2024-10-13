package com.ttalkak.compute.compute.domain

data class DockerDatabaseContainer(
    val databaseId: Long,
    val serviceType: ServiceType,
    val containerName: String,
    val subdomainKey: String,
    val dockerImageName: String,
    val dockerImageTag: String,
    val inboundPort: Int,
    var envs: List<Environment> = emptyList(),
    var outboundPort: Int = 0,
)
