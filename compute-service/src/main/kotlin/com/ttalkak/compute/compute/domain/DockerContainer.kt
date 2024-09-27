package com.ttalkak.compute.compute.domain

data class DockerContainer(
    private val deploymentId: Long,
    private val hasDockerImage: Boolean,
    private val containerName: String,
    private val inboundPort: Int,
    private val outboundPort: Int,
    private val subdomainName: String? = "",
    private val subdomainKey: String? = "",

    private val sourceCodeLink: String? = "",
    private val dockerRootDirectory: String? = "",
    private val hasDockerFile: Boolean,
    private val dockerFileScript: String? = "",
    private val envs: List<Pair<String, String>> = emptyList(),

    private val dockerImageName: String? = "",
    private val dockerImageTag: String? = "",
)
