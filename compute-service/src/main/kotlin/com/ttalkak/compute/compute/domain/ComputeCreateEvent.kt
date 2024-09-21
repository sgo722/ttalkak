package com.ttalkak.compute.compute.domain

data class ComputeCreateEvent(
    val deploymentId: Long,
    val env: String,
    val subdomainName: String,
    val subdomainKey: String,
    val serviceType: ServiceType,
    val port: Int,
    val repositoryUrl: String,
    val branch: String,
    val rootDirectory: String,
    val databases: List<Database>,
)
