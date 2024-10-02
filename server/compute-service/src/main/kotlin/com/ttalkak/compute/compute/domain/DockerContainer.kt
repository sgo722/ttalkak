package com.ttalkak.compute.compute.domain

data class DockerContainer(
    private val deploymentId: Long,
    val serviceType: ServiceType,
    private val hasDockerImage: Boolean,
    private val hasDockerFile: Boolean,

    private val sourceCodeLink: String? = "",
    private val dockerFileScript: String? = "",

    private val containerName: String,
    private val subdomainName: String? = "",
    private val subdomainKey: String? = "",
    private val dockerRootDirectory: String? = "",
    private val dockerImageName: String? = "",
    private val dockerImageTag: String? = "",

    val inboundPort: Int,
    var envs: List<Environment> = emptyList(),
    var outboundPort: Int = 0,
)
