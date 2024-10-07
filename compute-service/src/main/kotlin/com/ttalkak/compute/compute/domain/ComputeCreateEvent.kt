package com.ttalkak.compute.compute.domain

data class ComputeCreateEvent(
    val deploymentId: Long,
    val senderId: Long,
    val subdomainName: String,
    val subdomainKey: String,
    val serviceType: ServiceType = ServiceType.BACKEND,
    val repositoryUrl: String,
    val branch: String,
    val rootDirectory: String,
    val envs: List<Environment>,
    val port: Int,
    val version : Int,
    val dockerfileExist : Boolean,
    val dockerfileScript : String
)
