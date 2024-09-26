package com.ttalkak.deployment.deployment.framework.web;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ttalkak.deployment.deployment.application.usecase.*;
import com.ttalkak.deployment.deployment.domain.model.vo.DatabaseType;
import com.ttalkak.deployment.deployment.domain.model.vo.DeploymentStatus;
import com.ttalkak.deployment.deployment.domain.model.vo.ServiceType;
import com.ttalkak.deployment.deployment.framework.web.request.*;
import com.ttalkak.deployment.deployment.framework.web.response.*;
import com.ttalkak.deployment.deployment.support.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.stream.Collectors;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("배포 컨트롤러 테스트")
@WebMvcTest(
        value = DeploymentController.class,
        properties = {
                "spring.cloud.config.enabled=false",
                "subdomain.endpoint=localhost:8000"
        }
)
class DeploymentControllerTest extends RestDocsSupport {
    @MockBean
    private CreateDeploymentUsecase createDeploymentUsecase;

    @MockBean
    private CreateDockerFileUsecaseRegacy createDockerFileUsecaseRegacy;

    @MockBean
    private UpdateDeploymentUsecase updateDeploymentUsecase;

    @MockBean
    private DeleteDeploymentUsecase deleteDeploymentUsecase;

    @MockBean
    private CommandDeploymentStatusUsecase commandDeploymentStatusUsecase;

    @MockBean
    private InquiryUsecase inquiryUsecase;

    @Test
    @DisplayName("배포 생성")
    void createDeployment() throws Exception {
        // given
        GithubRepositoryRequest githubRepositoryRequest = createGitRepositoryRequest(
                "repoOwner",
                "repo-name",
                "https://github.com/repo-url",
                "root/",
                "main"
        );

        DatabaseCreateRequest databaseCreateRequest = createDatabaseRequest(
                DatabaseType.MYSQL, // Enum 타입으로 DatabaseType 사용
                3306,
                "dbUser",
                "dbPassword"
        );

        EnvCreateRequest env1 = createEnvRequest("key1", "value1");
        EnvCreateRequest env2 = createEnvRequest("key2", "value2");

        List<EnvCreateRequest> exampleEnvs = List.of(env1, env2);

        VersionRequest versionRequest = createVersionRequest(
                "Initial commit",
                "https://github.com/user/profile",
                "userName"
        );

        DeploymentCreateRequest request = DeploymentCreateRequest.builder()
                .projectId(1L)
                .serviceType("BACKEND")
                .githubRepositoryRequest(githubRepositoryRequest)
                .databaseCreateRequests(List.of(databaseCreateRequest))
                .versionRequest(versionRequest)
                .hostingPort(8080)
                .framework("Spring Boot")
                .envs(exampleEnvs)
                .build();

        // mocking response
        DeploymentCreateResponse response = DeploymentCreateResponse.of("https://ttalkak.com/webhook/deployment/backend/{webhooktoken}");

        when(createDeploymentUsecase.createDeployment(any(DeploymentCreateRequest.class)))
                .thenReturn(response);

        // when
        ResultActions perform = this.mockMvc.perform(
                post("/v1/deployment")
                        .content(toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        perform.andExpect(status().isCreated())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("배포")
                                .summary("배포 생성")
                                .description("사용자가 FRONTEND 혹은 BACKEND를 배포할 수 있습니다.")
                                .requestFields(
                                        fieldWithPath("projectId").type(JsonFieldType.NUMBER).description("프로젝트 ID"),
                                        fieldWithPath("framework").type(JsonFieldType.STRING).description("프레임워크"),
                                        fieldWithPath("serviceType").type(JsonFieldType.STRING).description("서비스 유형 (예: FRONTEND, BACKEND)"),
                                        fieldWithPath("githubRepositoryRequest.repositoryOwner").type(JsonFieldType.STRING).description("깃허브 레포지토리 오너"),
                                        fieldWithPath("githubRepositoryRequest.repositoryName").type(JsonFieldType.STRING).description("깃허브 저장소 이름"),
                                        fieldWithPath("githubRepositoryRequest.repositoryUrl").type(JsonFieldType.STRING).description("깃허브 저장소 URL"),
                                        fieldWithPath("versionRequest.repositoryLastCommitMessage").type(JsonFieldType.STRING).description("최근 커밋 메시지"),
                                        fieldWithPath("versionRequest.repositoryLastCommitUserProfile").type(JsonFieldType.STRING).description("최근 커밋 유저 프로필 URL"),
                                        fieldWithPath("versionRequest.repositoryLastCommitUserName").type(JsonFieldType.STRING).description("최근 커밋 유저 이름"),
                                        fieldWithPath("githubRepositoryRequest.rootDirectory").type(JsonFieldType.STRING).description("루트 디렉토리 경로"),
                                        fieldWithPath("githubRepositoryRequest.branch").type(JsonFieldType.STRING).description("깃허브 브랜치"),
                                        fieldWithPath("versionRequest.repositoryLastCommitMessage").type(JsonFieldType.STRING).description("깃허브 레포지토리 마지막 커밋 메시지"),
                                        fieldWithPath("versionRequest.repositoryLastCommitUserProfile").type(JsonFieldType.STRING).description("깃허브 레포지토리 커밋 유저 프로필 이미지"),
                                        fieldWithPath("versionRequest.repositoryLastCommitUserName").type(JsonFieldType.STRING).description("깃허브 레포지토리 마지막 커밋 유저 이름"),
                                        fieldWithPath("databaseCreateRequests[].databaseName").type(JsonFieldType.STRING).description("데이터베이스 이름"),
                                        fieldWithPath("databaseCreateRequests[].databasePort").type(JsonFieldType.NUMBER).description("데이터베이스 포트"),
                                        fieldWithPath("databaseCreateRequests[].username").type(JsonFieldType.STRING).optional().description("데이터베이스 사용자 이름"),
                                        fieldWithPath("databaseCreateRequests[].password").type(JsonFieldType.STRING).description("데이터베이스 비밀번호"),
                                        fieldWithPath("hostingPort").type(JsonFieldType.NUMBER).description("호스팅 포트"),
                                        fieldWithPath("envs[].key").type(JsonFieldType.STRING).description("환경변수 키 값"),
                                        fieldWithPath("envs[].value").type(JsonFieldType.STRING).description("환경변수 키에 해당하는 응답값")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지 (현재 null)"),
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드 (예: 201)"),
                                        fieldWithPath("data.webhookUrl").type(JsonFieldType.STRING).description("웹 훅 URL")
                                )
                                .build()
                )));
    }

    private GithubRepositoryRequest createGitRepositoryRequest(String repositoryOwner, String repositoryName, String repositoryUrl, String rootDirectory, String branch) {
        return GithubRepositoryRequest.builder()
                .repositoryOwner(repositoryOwner)
                .repositoryName(repositoryName)
                .repositoryUrl(repositoryUrl)
                .rootDirectory(rootDirectory)
                .branch(branch)
                .build();
    }

    private DatabaseCreateRequest createDatabaseRequest(DatabaseType databaseName, int databasePort, String username, String password) {
        return DatabaseCreateRequest.builder()
                .databaseName(databaseName)
                .databasePort(databasePort)
                .username(username)
                .password(password)
                .build();
    }

    private EnvCreateRequest createEnvRequest(String key, String value) {
        return EnvCreateRequest.builder()
                .key(key)
                .value(value)
                .build();
    }

    private VersionRequest createVersionRequest(String repositoryLastCommitMessage, String repositoryLastCommitUserProfile, String repositoryLastCommitUserName) {
        return VersionRequest.builder()
                .repositoryLastCommitMessage(repositoryLastCommitMessage)
                .repositoryLastCommitUserProfile(repositoryLastCommitUserProfile)
                .repositoryLastCommitUserName(repositoryLastCommitUserName)
                .build();
    }


    @Test
    @DisplayName("배포 상세조회")
    void getDeployment() throws Exception {
        // given
        DeploymentDetailResponse response = DeploymentDetailResponse.builder()
                .deploymentId(1L)
                .projectId(1L)
                .status(DeploymentStatus.PENDING)
                .serviceType(ServiceType.BACKEND)
                .repositoryName("repo-name")
                .repositoryOwner("https://github.com/repo-url")
                .repositoryUrl("sgo722")
                .branch("main")
                .framework("Spring Boot")
                .payloadURL("https://api.ttalkak.com/webhook/deployment/backend/{webhooktoken}")
                .hostingResponse(
                        HostingResponse.builder()
                                .hostingId(1L)
                                .serviceType(ServiceType.BACKEND)
                                .detailDomainName("backend.ttalkak.com")
                                .hostingPort(8080)
                                .build()
                )
                .envs(List.of(
                        EnvResponse.builder().envId(1L).key("key1").value("value1").build(),
                        EnvResponse.builder().envId(2L).key("key2").value("value2").build()
                ))
                .databaseResponses(List.of(
                        DatabaseResponse.builder()
                                .databaseId(1L)
                                .databaseType("MYSQL")
                                .username("dbUser")
                                .password("dbPassword")
                                .port(3306)
                                .build(),
                        DatabaseResponse.builder()
                                .databaseId(2L)
                                .databaseType("REDIS")
                                .username("redisUser")
                                .password("redisPassword")
                                .port(6379)
                                .build()
                ))
                .versions(List.of(
                        VersionResponse.builder()
                                .id(1L)
                                .version(1L)
                                .logUrl("https://ttalkak.com/logs/1")
                                .repositoryLastCommitMessage("Initial commit")
                                .repositoryLastCommitUserProfile("https://github.com/user/profile")
                                .repositoryLastCommitUserName("userName")
                                .build()
                ))
                .build();

        when(inquiryUsecase.getDeployment(anyLong())).thenReturn(response);

        // when
        ResultActions perform = mockMvc.perform(
                        get("/v1/deployment/{deploymentId}", 1L)
                                .accept(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("배포")
                                .summary("배포 상세 조회")
                                .description("사용자가 FRONTEND 혹은 BACKEND를 배포할 수 있습니다.")
                                .pathParameters(
                                parameterWithName("deploymentId").description("조회할 배포의 ID")
                                )
                                .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지 (예: 'OK')"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드 (예: 200)"),
                                fieldWithPath("data.deploymentId").type(JsonFieldType.NUMBER).description("배포의 ID"),
                                fieldWithPath("data.projectId").type(JsonFieldType.NUMBER).description("프로젝트의 ID"),
                                fieldWithPath("data.status").type(JsonFieldType.STRING).description("배포 상태 (예: READY, RUNNING, ERROR)"),
                                fieldWithPath("data.serviceType").type(JsonFieldType.STRING).description("서비스 유형 (예: FRONTEND, BACKEND)"),
                                fieldWithPath("data.repositoryName").type(JsonFieldType.STRING).description("깃허브 저장소 이름"),
                                fieldWithPath("data.repositoryUrl").type(JsonFieldType.STRING).description("깃허브 저장소 URL"),
                                fieldWithPath("data.repositoryOwner").type(JsonFieldType.STRING).description("깃허브 레포지토리 오너"),
                                fieldWithPath("data.branch").type(JsonFieldType.STRING).description("깃허브 브랜치"),
                                fieldWithPath("data.framework").type(JsonFieldType.STRING).description("사용된 프레임워크"),
                                fieldWithPath("data.payloadURL").type(JsonFieldType.STRING).description("Payload URL"),
                                fieldWithPath("data.envs").type(JsonFieldType.ARRAY).description("환경 변수 목록"),
                                fieldWithPath("data.envs[].envId").type(JsonFieldType.NUMBER).description("환경 변수 ID"),
                                fieldWithPath("data.envs[].key").type(JsonFieldType.STRING).description("환경 변수 키"),
                                fieldWithPath("data.envs[].value").type(JsonFieldType.STRING).description("환경 변수 값"),
                                fieldWithPath("data.hostingResponse").type(JsonFieldType.OBJECT).description("호스팅 정보"),
                                fieldWithPath("data.hostingResponse.hostingId").type(JsonFieldType.NUMBER).description("호스팅 ID"),
                                fieldWithPath("data.hostingResponse.serviceType").type(JsonFieldType.STRING).description("호스팅 서비스 타입"),
                                fieldWithPath("data.hostingResponse.detailDomainName").type(JsonFieldType.STRING).description("호스팅 서브 도메인 이름"),
                                fieldWithPath("data.hostingResponse.hostingPort").type(JsonFieldType.NUMBER).description("호스팅 포트"),
                                fieldWithPath("data.databaseResponses").type(JsonFieldType.ARRAY).description("데이터베이스 응답 목록").optional(),
                                fieldWithPath("data.databaseResponses[].databaseId").type(JsonFieldType.NUMBER).description("데이터베이스 ID"),
                                fieldWithPath("data.databaseResponses[].databaseType").type(JsonFieldType.STRING).description("데이터베이스 타입"),
                                fieldWithPath("data.databaseResponses[].username").type(JsonFieldType.STRING).description("데이터베이스 사용자 이름"),
                                fieldWithPath("data.databaseResponses[].password").type(JsonFieldType.STRING).description("데이터베이스 비밀번호"),
                                fieldWithPath("data.databaseResponses[].port").type(JsonFieldType.NUMBER).description("데이터베이스 포트번호"),
                                fieldWithPath("data.versions").type(JsonFieldType.ARRAY).description("버전 정보 목록").optional(),
                                fieldWithPath("data.versions[].id").type(JsonFieldType.NUMBER).description("버전 ID"),
                                fieldWithPath("data.versions[].version").type(JsonFieldType.NUMBER).description("버전 번호"),
                                fieldWithPath("data.versions[].logUrl").type(JsonFieldType.STRING).description("로그 URL"),
                                fieldWithPath("data.versions[].repositoryLastCommitMessage").type(JsonFieldType.STRING).description("최근 커밋 메시지"),
                                fieldWithPath("data.versions[].repositoryLastCommitUserProfile").type(JsonFieldType.STRING).description("최근 커밋 유저 프로필 URL"),
                                fieldWithPath("data.versions[].repositoryLastCommitUserName").type(JsonFieldType.STRING).description("최근 커밋 유저 이름")
                        ).build()
                )));
    }

//    @DisplayName("배포 검색조회")
//    @Test
//    void searchDeploymentByGithubRepositoryName() throws Exception {
//        // given
//        DeploymentPreviewResponse response1 = DeploymentPreviewResponse.builder()
//                .deploymentId(1L)
//                .projectId(1L)
//                .statu
//                .status(DeploymentStatus.RUNNING)
//                .serviceType(ServiceType.BACKEND)
//                .build();
//
//        DeploymentPreviewResponse response2 = DeploymentPreviewResponse.builder()
//                .deploymentId(2L)
//                .projectId(2L)
//                .status(DeploymentStatus.PENDING)
//                .serviceType(ServiceType.FRONTEND)
//                .build();
//
//        when(inquiryUsecase.searchDeploymentByGithubRepositoryName(anyString(), anyInt(), anyInt()))
//                .thenReturn(List.of(response1, response2));
//
//        // when
//        ResultActions perform = mockMvc.perform(
//                        get("/v1/deployment")
//                                .param("githubRepoName", "repo")
//                                .param("page", "0")
//                                .param("size", "10")
//                                .accept(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("deployment-search",
//                        //JSON 이쁘게 만들기
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        //RestDoc 스니펫 정보
//                        queryParameters(
//                                parameterWithName("githubRepoName").description("검색할 깃허브 저장소 이름"),
//                                parameterWithName("page").optional().description("페이지 번호 (기본값: 0)"),
//                                parameterWithName("size").optional().description("페이지 크기 (기본값: 10)")
//                        ),responseFields(
//                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
//                                        .description("요청 성공 여부"),
//                                fieldWithPath("message").type(JsonFieldType.STRING).optional()
//                                        .description("응답 메시지"),
//                                fieldWithPath("status").type(JsonFieldType.NUMBER)
//                                        .description("응답 코드"),
//                                fieldWithPath("data").type(JsonFieldType.ARRAY)
//                                        .description("배포 응답 목록"),
//                                fieldWithPath("data[].deploymentId").type(JsonFieldType.NUMBER)
//                                        .description("배포 ID"),
//                                fieldWithPath("data[].projectId").type(JsonFieldType.NUMBER)
//                                        .description("프로젝트 ID"),
//                                fieldWithPath("data[].status").type(JsonFieldType.STRING)
//                                        .description("배포 상태 (예: READY, RUNNING, ERROR)"),
//                                fieldWithPath("data[].serviceType").type(JsonFieldType.STRING)
//                                        .description("서비스 유형 (예: FRONTEND, BACKEND)"),
//                                fieldWithPath("data[].repositoryLastCommitMessage").type(JsonFieldType.STRING).optional()
//                                        .description("최근 커밋 메시지"),
//                                fieldWithPath("data[].repositoryLastCommitUserProfile").type(JsonFieldType.STRING).optional()
//                                        .description("최근 커밋 유저 프로필 URL"),
//                                fieldWithPath("data[].repositoryLastCommitUserName").type(JsonFieldType.STRING).optional()
//                                        .description("최근 커밋 유저 이름"),
//                                fieldWithPath("data[].branch").type(JsonFieldType.STRING).optional()
//                                        .description("깃허브 브랜치"),
//                                fieldWithPath("data[].framework").type(JsonFieldType.STRING).optional()
//                                        .description("사용된 프레임워크")
//                        )
//                ));
//    }


    @DisplayName("배포 삭제")
    @Test
    void deleteDeployment() throws Exception {
        //given
        Long deploymentId = 1L;

        //when
        ResultActions perform = mockMvc.perform(
                        delete("/v1/deployment/{deploymentId}", deploymentId)
                                .header("X-USER-ID", 1L)  // 헤더 추가
                                .accept(MediaType.APPLICATION_JSON)
                );
        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                        .tag("배포")
                        .summary("배포 삭제")
                        .description("사용자가 배포했던 기록을 삭제할 수 있습니다.")
                        .pathParameters(
                                parameterWithName("deploymentId").description("배포 ID")
                        )
                        .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP 상태 코드"),
                                fieldWithPath("data").type(JsonFieldType.NULL).description("삭제된 후에는 반환되는 데이터가 없습니다.")
                        ).build())
                ));
    }

//    @DisplayName("배포 상태 변경")
//    @Test
//    public void updateDeploymentStatus() throws Exception {
//        // given
//        // DeploymentCommandStatusRequest에 필요한 값을 설정
//        DeploymentCommandStatusRequest request = DeploymentCommandStatusRequest.builder()
//                .deploymentId(1L)
//                .command(CommandEvent.START)
//                .build();
//        // when - then
//        mockMvc.perform(post("/v1/deployment/status")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("deployment-status",
//                        //JSON 이쁘게 만들기
//                        preprocessRequest(prettyPrint()),
//                        preprocessResponse(prettyPrint()),
//                        //RestDoc 스니펫 정보
//                        requestFields(
//                                fieldWithPath("deploymentId").type(JsonFieldType.NUMBER)
//                                         .description("배포 ID"),
//                                fieldWithPath("command").type(JsonFieldType.STRING)
//                                        .description("배포 상태 변경 메시지(예 : RESTART, START, STOP)")
//                        ),
//                        responseFields(
//                                fieldWithPath("success").type(JsonFieldType.BOOLEAN)
//                                        .description("요청 성공 여부"),
//                                fieldWithPath("message").type(JsonFieldType.STRING)
//                                        .description("응답 메시지 (현재 null)"),
//                                fieldWithPath("status").type(JsonFieldType.NUMBER)
//                                        .description("응답 코드 (예: 200)"),
//                                fieldWithPath("data").description("Always null for this request").optional().type(JsonFieldType.NULL)
//
//                        )
//                ));
//
//        // verify
//        Mockito.verify(commandDeploymentStatusUsecase, Mockito.times(1)).commandDeploymentStatus(Mockito.any(DeploymentCommandStatusRequest.class));
//    }
//
    @DisplayName("배포 수정")
    @Test
    void updateDeployment() throws Exception {
        // given
        GithubRepositoryRequest githubRepositoryRequest = GithubRepositoryRequest.builder()
                .repositoryName("repo-name")
                .repositoryUrl("https://github.com/repo-url")
                .rootDirectory("root/")
                .branch("main")
                .build();

        EnvUpdateRequest env1 = new EnvUpdateRequest("key1", "value1");
        EnvUpdateRequest env2 = new EnvUpdateRequest("key2", "value2");

        List<EnvUpdateRequest> envs = List.of(env1, env2);

        DeploymentUpdateRequest request = DeploymentUpdateRequest.builder()
                .deploymentId(1L)
                .githubRepositoryRequest(githubRepositoryRequest)
                .hostingPort(8080)
                .envs(envs)
                .build();

        VersionResponse versionResponse1 = VersionResponse.builder()
                .id(1L)
                .version(1L)
                .logUrl("s3 link")
                .repositoryLastCommitMessage("feat : 생성 기능")
                .repositoryLastCommitUserName("sgo722")
                .repositoryLastCommitUserProfile("https://avatars.githubusercontent.com/u/88089193?s=400&u=23e66c8a4b8160d64b3f10887576034d2bccec21&v=4")
                .build();
        VersionResponse versionResponse2 = VersionResponse.builder()
                .id(2L)
                .version(2L)
                .logUrl("s3 link")
                .repositoryLastCommitMessage("feat : 수정 기능")
                .repositoryLastCommitUserName("sunsuking")
                .repositoryLastCommitUserProfile("https://avatars.githubusercontent.com/u/88089193?s=400&u=23e66c8a4b8160d64b3f10887576034d2bccec21&v=4")
                .build();

        List<VersionResponse> versionResponses = List.of(versionResponse1, versionResponse2);


        DeploymentDetailResponse response = DeploymentDetailResponse.builder()
                .deploymentId(1L)
                .projectId(1L)
                .status(DeploymentStatus.RUNNING)
                .serviceType(ServiceType.BACKEND)
                .repositoryName("repo-name")
                .repositoryUrl("https://github.com/repo-url")
                .repositoryOwner("sgo722")
                .branch("main")
                .framework("Spring Boot")
                .payloadURL("webhookToken관련 URL")
                .envs(envs.stream().map(env -> new EnvResponse(env.getKey(), env.getValue())).collect(Collectors.toList()))
                .versions(versionResponses)
                .hostingResponse(
                        HostingResponse.builder()
                                .hostingId(1L)
                                .serviceType(ServiceType.BACKEND)
                                .detailDomainName("backend.ttalkak.com")
                                .hostingPort(8080)
                                .build()
                )
                .build();


        when(updateDeploymentUsecase.updateDeployment(any(Long.class), any(DeploymentUpdateRequest.class)))
                .thenReturn(response);

        // when
        // when
        ResultActions perform = mockMvc.perform(
                        patch("/v1/deployment")
                                .content(toJson(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("X-USER-ID", 1L)
                );
        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .requestFields(
                                fieldWithPath("deploymentId").type(JsonFieldType.NUMBER).description("수정할 배포의 ID"),
                                fieldWithPath("hostingPort").type(JsonFieldType.NUMBER).description("호스팅 포트"),
                                fieldWithPath("githubRepositoryRequest.repositoryName").type(JsonFieldType.STRING).description("깃허브 저장소 이름"),
                                fieldWithPath("githubRepositoryRequest.repositoryUrl").type(JsonFieldType.STRING).description("최근 커밋 유저 이름"),
                                fieldWithPath("githubRepositoryRequest.rootDirectory").type(JsonFieldType.STRING).description("루트 디렉토리 경로"),
                                fieldWithPath("githubRepositoryRequest.branch").type(JsonFieldType.STRING).description("깃허브 브랜치"),
                                fieldWithPath("envs[].key").type(JsonFieldType.STRING).description("환경 변수 키"),
                                fieldWithPath("envs[].value").type(JsonFieldType.STRING).description("환경 변수 값"),
                                fieldWithPath("databaseUpdateRequests").type(JsonFieldType.ARRAY).optional().description("데이터베이스 업데이트 요청 목록"),
                                fieldWithPath("databaseUpdateRequests[].id").type(JsonFieldType.NUMBER).optional().description("데이터베이스 ID"),
                                fieldWithPath("databaseUpdateRequests[].databaseType").type(JsonFieldType.STRING).optional().description("데이터베이스 타입"),
                                fieldWithPath("databaseUpdateRequests[].username").type(JsonFieldType.STRING).optional().description("데이터베이스 사용자 이름"),
                                fieldWithPath("databaseUpdateRequests[].password").type(JsonFieldType.STRING).optional().description("데이터베이스 비밀번호"),
                                fieldWithPath("databaseUpdateRequests[].port").type(JsonFieldType.NUMBER).optional().description("데이터베이스 포트번호")
                        )
                                .responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                fieldWithPath("message").type(JsonFieldType.STRING).optional().description("응답 메시지 (예: 'OK')"),
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드 (예: 201)"),
                                fieldWithPath("data.deploymentId").type(JsonFieldType.NUMBER).description("수정된 배포의 ID"),
                                fieldWithPath("data.projectId").type(JsonFieldType.NUMBER).description("프로젝트의 ID"),
                                fieldWithPath("data.status").type(JsonFieldType.STRING).description("배포 상태 (예: READY, RUNNING, ERROR)"),
                                fieldWithPath("data.serviceType").type(JsonFieldType.STRING).description("서비스 유형 (예: FRONTEND, BACKEND)"),
                                fieldWithPath("data.repositoryName").type(JsonFieldType.STRING).description("깃허브 저장소 이름"),
                                fieldWithPath("data.repositoryUrl").type(JsonFieldType.STRING).description("깃허브 저장소 URL"),
                                fieldWithPath("data.payloadURL").type(JsonFieldType.STRING).description("payloadURL"),
                                fieldWithPath("data.versions[].id").type(JsonFieldType.NUMBER).description("버전 PK"),
                                fieldWithPath("data.versions[].version").type(JsonFieldType.NUMBER).description("해당 배포된 프로젝트에 대한 버전"),
                                fieldWithPath("data.versions[].logUrl").type(JsonFieldType.STRING).description("배포된 프로젝트 logURL"),
                                fieldWithPath("data.repositoryOwner").type(JsonFieldType.STRING).optional().description("레포지토리 주인"),
                                fieldWithPath("data.versions[].repositoryLastCommitMessage").type(JsonFieldType.STRING).description("깃허브 레포지토리 마지막 커밋 메시지"),
                                fieldWithPath("data.versions[].repositoryLastCommitUserProfile").type(JsonFieldType.STRING).description("깃허브 레포지토리 커밋 유저 프로필 이미지"),
                                fieldWithPath("data.versions[].repositoryLastCommitUserName").type(JsonFieldType.STRING).description("깃허브 레포지토리 마지막 커밋 유저 이름"),fieldWithPath("data.repositoryLastCommitMessage").type(JsonFieldType.STRING).optional().description("최근 커밋 메시지"),
                                fieldWithPath("data.branch").type(JsonFieldType.STRING).description("깃허브 브랜치"),
                                fieldWithPath("data.framework").type(JsonFieldType.STRING).description("사용된 프레임워크"),
                                fieldWithPath("data.envs").type(JsonFieldType.ARRAY).description("환경 변수 목록"),
                                fieldWithPath("data.envs[].envId").type(JsonFieldType.NUMBER).optional().description("환경 변수 ID"), // 추가된 필드
                                fieldWithPath("data.envs[].key").type(JsonFieldType.STRING).description("환경 변수 키"),
                                fieldWithPath("data.envs[].value").type(JsonFieldType.STRING).description("환경 변수 값"),
                                fieldWithPath("data.databaseResponses").type(JsonFieldType.ARRAY).optional().description("데이터베이스 응답 목록"),
                                fieldWithPath("data.hostingResponse").type(JsonFieldType.OBJECT).description("호스팅 정보"),
                                fieldWithPath("data.hostingResponse.hostingId").type(JsonFieldType.NUMBER).description("호스팅 ID"),
                                fieldWithPath("data.hostingResponse.serviceType").type(JsonFieldType.STRING).description("호스팅 서비스 타입"),
                                fieldWithPath("data.hostingResponse.detailDomainName").type(JsonFieldType.STRING).description("호스팅 서브 도메인 이름"),
                                fieldWithPath("data.hostingResponse.hostingPort").type(JsonFieldType.NUMBER).description("호스팅 포트")
                        ).build()
                )));
    }
}