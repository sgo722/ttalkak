package com.ttalkak.compute.compute.domain

data class ComputeCreateEvent(
    val deploymentId: Long,
    val subdomainName: String,
    val subdomainKey: String,
    val serviceType: ServiceType,
    val repositoryUrl: String,
    val branch: String,
    val rootDirectory: String,
    val databases: List<Database>,
    val envs: List<Environment>,
    val port: Int,
    val version : Int,
    val dockerfileExist : Boolean,
    val dockerfileScript : String
)
