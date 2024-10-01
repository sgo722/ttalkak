package com.ttalkak.compute.compute.domain

data class DockerContainer(
    private val deploymentId: Long,
    private val serviceType: String,
    private val hasDockerImage: Boolean,
    private val hasDockerFile: Boolean,

    private val sourceCodeLink: String? = "",
    private val dockerFileScript: String? = "",

    private val containerName: String,
    private val subdomainName: String? = "",
    private val subdomainKey: String? = "",
    private val dockerRootDirectory: String? = "",
    private val envs: List<Environment> = emptyList(),
    private val dockerImageName: String? = "",
    private val dockerImageTag: String? = "",

    val inboundPort: Int,
    var outboundPort: Int = 0,
)
