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


    // 변경되어야하는 데이터
    /** env파일이 list형식으로 넘어가기에 변경해야합니다.
     *
     * 카프카를 통해 넘어오는 형태는 아래와 같습니다.
     * ```
     * private List<EnvEvent> envs;
     *
     *
     * public class EnvEvent implements Serializable {
     *
     *     private String key;
     *
     *     private String value;
     * }
     * ```
     */
    val env: String,


    /** port 데이터 타입 변경해야합니다.
     *
     * 카프카를 통해 넘어오는 형태는 아래와 같습니다.
     * ```
     * private String port;
     */
    val port: Int,

    // 추가된 데이터
    val version : Long,

    val dockerfileExist : Boolean,

    val dockerfileScript : String

)
