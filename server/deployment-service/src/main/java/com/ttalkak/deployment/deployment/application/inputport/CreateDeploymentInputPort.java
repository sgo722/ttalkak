package com.ttalkak.deployment.deployment.application.inputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ttalkak.deployment.deployment.application.outputport.*;
import com.ttalkak.deployment.deployment.application.usecase.CreateDeploymentUsecase;
import com.ttalkak.deployment.deployment.application.usecase.CreateDockerfileUseCase;
import com.ttalkak.deployment.deployment.domain.event.CreateInstanceEvent;
import com.ttalkak.deployment.deployment.domain.event.DatabaseEvent;
import com.ttalkak.deployment.deployment.domain.event.DeploymentEvent;
import com.ttalkak.deployment.deployment.domain.event.HostingEvent;
import com.ttalkak.deployment.deployment.domain.event.*;
import com.ttalkak.deployment.deployment.domain.model.*;
import com.ttalkak.deployment.deployment.domain.model.vo.GithubInfo;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DomainKeyResponse;
import com.ttalkak.deployment.deployment.framework.domainadapter.dto.DomainRequest;
import com.ttalkak.deployment.deployment.framework.projectadapter.dto.ProjectInfoResponse;
import com.ttalkak.deployment.deployment.framework.web.request.DatabaseCreateRequest;
import com.ttalkak.deployment.deployment.framework.web.request.DeploymentCreateRequest;
import com.ttalkak.deployment.deployment.framework.web.request.DockerfileCreateRequest;
import com.ttalkak.deployment.deployment.framework.web.request.EnvCreateRequest;
import com.ttalkak.deployment.deployment.framework.web.response.DeploymentCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateDeploymentInputPort implements CreateDeploymentUsecase {

    private final CreateDockerfileUseCase createDockerfileUseCase;

    private final DeploymentOutputPort deploymentOutputPort;

    private final HostingOutputPort hostingOutputPort;

    private final VersionOutputPort versionOutputPort;

    private final DatabaseOutputPort databaseOutputPort;

    private final EnvOutputPort envOutputPort;

    private final ProjectOutputPort projectOutputPort;

    private final DomainOutputPort domainOutputPort;

    private final EventOutputPort eventOutputPort;

    @Override
    public DeploymentCreateResponse createDeployment(DeploymentCreateRequest deploymentCreateRequest) {
        // 깃허브 관련 정보 객체 생성
        GithubInfo githubInfo = createGithubInfo(deploymentCreateRequest);

        // 프로젝트 서비스로부터 도메인 이름 받아오기
        ProjectInfoResponse projectInfo = projectOutputPort.getProjectInfo(deploymentCreateRequest.getProjectId());
        String domainName = projectInfo.getDomainName();
        String webhookToken = projectInfo.getWebhookToken();
        String payloadURL = "https://api.ttalkak.com/webhook/deployment/" + deploymentCreateRequest.getServiceType().toLowerCase() + "/" + webhookToken;
        String expirationDate = projectInfo.getExpirationDate();

        // 배포 객체 생성
        DeploymentEntity deployment = createDeployment(deploymentCreateRequest, githubInfo, payloadURL);
        // 배포 객체 저장
        DeploymentEntity savedDeployment = deploymentOutputPort.save(deployment);

        DockerfileCreateRequest dockerfileCreateRequest = deploymentCreateRequest.getDockerfileCreateRequest();

        boolean dockerfileExist = dockerfileCreateRequest.getExist();
        if(!dockerfileExist){
            String dockerFileScript = createDockerfileUseCase.generateDockerfileScript(deployment.getServiceType(),
                    dockerfileCreateRequest.getBuildTool(),
                    dockerfileCreateRequest.getPackageManager(),
                    dockerfileCreateRequest.getLanguageVersion()
            );
            deployment.setDockerfileScript(dockerFileScript);
        }

        // 배포 버전 객체 생성
        VersionEntity versionEntity = createVersion(deploymentCreateRequest, 1L, savedDeployment);

        VersionEntity savedVersionEntity = versionOutputPort.save(versionEntity);

        // 배포 버전 저장
        savedDeployment.addVersion(savedVersionEntity);

        // 호스팅 객체 생성
        HostingEntity hosting = createHosting(deploymentCreateRequest, savedDeployment, domainName);

        // 호스팅 객체 저장
        HostingEntity savedHostingEntity = hostingOutputPort.save(hosting);

        // 도메인 서비스로부터 도메인 키 생성
        String detailSubDomainKey = makeSubDomainKey(savedHostingEntity, projectInfo);

        // 도메인 키 저장
        savedHostingEntity.setDetailSubDomainKey(detailSubDomainKey);

        // 백엔드 서버면? =>  데이터베이스가 존재한다.
        List<DatabaseEvent> databaseEvents = createDatabaseEvents(deploymentCreateRequest, savedDeployment);


        // 환경변수 생성
        List<EnvEvent> envs = createEnvs(deploymentCreateRequest, deployment, savedDeployment);
        // Kafka Event 객체 생성
        HostingEvent hostingEvent = new HostingEvent(savedDeployment.getId(), savedHostingEntity.getId(), savedHostingEntity.getHostingPort(), null,projectInfo.getDomainName(), hosting.getDetailSubDomainKey());
        DeploymentEvent deploymentEvent = new DeploymentEvent(savedDeployment.getId(), savedDeployment.getProjectId(), envs, savedDeployment.getServiceType().toString());
        GithubInfoEvent githubInfoEvent = new GithubInfoEvent(deployment.getGithubInfo().getRepositoryUrl(), deployment.getGithubInfo().getRootDirectory(), deployment.getGithubInfo().getBranch());
        CreateInstanceEvent createInstanceEvent = new CreateInstanceEvent(deploymentEvent, hostingEvent, githubInfoEvent, envs, databaseEvents, versionEntity.getVersion(), expirationDate, dockerfileExist, deployment.getDockerfileScript());
        try {
            eventOutputPort.occurCreateInstance(createInstanceEvent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카프카 요청 오류가 발생했습니다.");
        }

        return DeploymentCreateResponse.of(payloadURL);
    }

    private static VersionEntity createVersion(DeploymentCreateRequest deploymentCreateRequest, Long versionId, DeploymentEntity savedDeployment) {
        return VersionEntity.createVersion(savedDeployment,
                versionId,
                deploymentCreateRequest.getVersionRequest().getRepositoryLastCommitMessage(),
                deploymentCreateRequest.getVersionRequest().getRepositoryLastCommitUserProfile(),
                deploymentCreateRequest.getVersionRequest().getRepositoryLastCommitUserName());
    }

    private List<EnvEvent> createEnvs(DeploymentCreateRequest deploymentCreateRequest, DeploymentEntity deployment, DeploymentEntity savedDeployment) {
        List<EnvEvent> envs = new ArrayList<>();
        if(deploymentCreateRequest.getEnvs().size() > 0) {
            for (EnvCreateRequest envCreateRequest : deploymentCreateRequest.getEnvs()) {
                EnvEntity env = EnvEntity.create(envCreateRequest.getKey(), envCreateRequest.getValue(), deployment);
                EnvEntity savedEnv = envOutputPort.save(env);
                savedDeployment.createEnv(savedEnv);
                envs.add(new EnvEvent(envCreateRequest.getKey(), envCreateRequest.getValue()));
            }
        }
        return envs;
    }

    private List<DatabaseEvent> createDatabaseEvents(DeploymentCreateRequest deploymentCreateRequest, DeploymentEntity savedDeployment) {
        List<DatabaseEvent> databaseEvents = new ArrayList<>();
        if(ServiceType.isBackendType(deploymentCreateRequest.getServiceType())) {
            if (deploymentCreateRequest.getDatabaseCreateRequests() != null) {
                for (DatabaseCreateRequest databaseCreateRequest : deploymentCreateRequest.getDatabaseCreateRequests()) {
                    DatabaseEntity database = DatabaseEntity.createDatabase(
                            savedDeployment,
                            databaseCreateRequest.getDatabasePort(),
                            // 이후에 고쳐야할 내용(프론트에서 데이터베이스 이름 넘겨주는 작업 완료 시 수정해야함)
                            databaseCreateRequest.getDatabaseName().toString().toLowerCase(),
                            databaseCreateRequest.getDatabaseName(),
                            databaseCreateRequest.getUsername(),
                            databaseCreateRequest.getPassword()
                    );
                    DatabaseEntity savedDatabaseEntity = databaseOutputPort.save(database);
                    savedDeployment.addDatabaseEntity(savedDatabaseEntity);
                    databaseEvents.add(new DatabaseEvent(savedDatabaseEntity.getId(),
                            savedDatabaseEntity.getName(),
                            savedDatabaseEntity.getDatabaseType().toString(),
                            savedDatabaseEntity.getUsername(),
                            savedDatabaseEntity.getPassword(),
                            savedDatabaseEntity.getPort()
                    ));
                }
            }
        }
        return databaseEvents;
    }

    private String makeSubDomainKey(HostingEntity savedHostingEntity, ProjectInfoResponse projectInfo) {
        DomainKeyResponse domainKeyResponse = domainOutputPort.makeDomainKey(
                new DomainRequest(
                        savedHostingEntity.getId().toString(),
                        projectInfo.getDomainName() + " " + savedHostingEntity.getServiceType().toString(),
                        savedHostingEntity.getDetailSubDomainName()
                ));
        return domainKeyResponse.getKey();
    }

    private static HostingEntity createHosting(DeploymentCreateRequest deploymentCreateRequest, DeploymentEntity savedDeployment, String domainName) {
        return HostingEntity.createHosting(
                deploymentCreateRequest.getHostingPort(),
                deploymentCreateRequest.getProjectId(),
                deploymentCreateRequest.getServiceType(),
                domainName
        );
    }

    private static DeploymentEntity createDeployment(DeploymentCreateRequest deploymentCreateRequest, GithubInfo githubInfo, String payloadURL) {
        return DeploymentEntity.createDeployment(
                deploymentCreateRequest.getProjectId(),
                ServiceType.valueOf(deploymentCreateRequest.getServiceType()),
                githubInfo,
                deploymentCreateRequest.getFramework(),
                payloadURL);
    }

    private static GithubInfo createGithubInfo(DeploymentCreateRequest deploymentCreateRequest) {
        return GithubInfo.create(
                deploymentCreateRequest.getGithubRepositoryRequest().getRepositoryOwner(),
                deploymentCreateRequest.getGithubRepositoryRequest().getRepositoryName(),
                deploymentCreateRequest.getGithubRepositoryRequest().getRepositoryUrl(),
                deploymentCreateRequest.getGithubRepositoryRequest().getRootDirectory(),
                deploymentCreateRequest.getGithubRepositoryRequest().getBranch()
        );
    }
}
