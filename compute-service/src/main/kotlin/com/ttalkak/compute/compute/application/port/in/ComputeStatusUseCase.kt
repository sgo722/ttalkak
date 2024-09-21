package com.ttalkak.compute.compute.application.port.`in`

interface ComputeStatusUseCase {
    /**
     * 컴퓨터 상태를 업데이트한다.
     *
     * @param connectCommand 연결 정보
     * @param deploymentCommands 배포 정보
     * @return Unit
     */
    fun update(connectCommand: ConnectCommand, deploymentCommands: List<DeploymentCommand>)
}