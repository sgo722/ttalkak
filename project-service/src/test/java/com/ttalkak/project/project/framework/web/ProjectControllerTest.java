package com.ttalkak.project.project.framework.web;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ttalkak.project.project.application.usecase.CreateProjectUseCase;
import com.ttalkak.project.project.application.usecase.DeleteProjectUseCase;
import com.ttalkak.project.project.application.usecase.GetProjectUseCase;
import com.ttalkak.project.project.application.usecase.UpdateProjectUseCase;
import com.ttalkak.project.project.config.RestDocsUtils;
import com.ttalkak.project.project.framework.deploymentadapter.dto.DeploymentResponse;
import com.ttalkak.project.project.framework.deploymentadapter.dto.EnvResponse;
import com.ttalkak.project.project.framework.deploymentadapter.dto.HostingResponse;
import com.ttalkak.project.project.framework.web.request.DomainNameRequest;
import com.ttalkak.project.project.framework.web.request.ProjectCreateRequest;
import com.ttalkak.project.project.framework.web.request.ProjectUpdateRequest;
import com.ttalkak.project.project.framework.web.response.*;
import com.ttalkak.project.project.support.RestDocsSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 컨트롤러 테스트")
@WebMvcTest(
        value = ProjectController.class,
        properties = {
                "spring.cloud.config.enabled=false",
                "subdomain.endpoint=localhost:8000"
        }
)
class ProjectControllerTest extends RestDocsSupport {


    @MockBean
    private CreateProjectUseCase createProjectUseCase;

    @MockBean
    private GetProjectUseCase getProjectUseCase;

    @MockBean
    private UpdateProjectUseCase updateProjectUseCase;

    @MockBean
    private DeleteProjectUseCase deleteProjectUseCase;

    LocalDateTime fixedClock = LocalDateTime.of(2024, 1, 1, 0, 0, 0,0);

    @Test
    @DisplayName("프로젝트 생성 테스트")
    void 프로젝트_생성_테스트() throws Exception {

        // given
        ProjectCreateRequest projectCreateRequest = new ProjectCreateRequest(
                "project",
                "domain",
                "9999-12-31");

        ProjectCreateResponse projectCreateResponse = ProjectCreateResponse.builder()
                .id(1L)
                .userId(1L)
                .projectName("project")
                .domainName("domain")
                .expirationDate("9999-12-31")
                .createdAt(fixedClock)
                .updatedAt(fixedClock)
                .build();

        Long userId = 1L;
        when(createProjectUseCase.createProject(eq(userId), any(ProjectCreateRequest.class)))
                .thenReturn(projectCreateResponse);

        // when
        ResultActions perform = mockMvc.perform(
                post("/v1/project")
                        .header("X-USER-ID", 1L)
                        .content(toJson(projectCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)

        );

        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("프로젝트")
                                .summary("프로젝트 생성")
                                .description("사용자는 프로젝트를 생성할 수 있습니다.")
                                .requestFields(
                                        fieldWithPath("projectName").type(JsonFieldType.STRING)
                                                .description("프로젝트명"),
                                        fieldWithPath("domainName").type(JsonFieldType.STRING)
                                                .description("도메인명"),
                                        fieldWithPath("expirationDate").type(JsonFieldType.STRING)
                                                .description("결제 만료기간")
                                )
                                .responseFields(
                                        RestDocsUtils.combineFields(
                                                RestDocsUtils.getCommonResponseFields(),
                                                RestDocsUtils.createProjectResponseFields()
                                        )
                                )
                                .build()
                )));

        // then
        perform.andDo(print());
    }

    @Test
    @DisplayName("프로젝트 조회 테스트")
    void 프로젝트_조회_테스트() throws Exception{

        // given
        EnvResponse env1 = EnvResponse.builder()
                .envId("1")
                .key("key1")
                .value("value1")
                .build();

        EnvResponse env2 = EnvResponse.builder()
                .envId("2")
                .key("key2")
                .value("value2")
                .build();

        List<EnvResponse> envResponses = List.of(env1, env2);

        HostingResponse hostingResponse = HostingResponse.builder()
                .hostingId(1L)
                .hostingPort(8080)
                .serviceType("BACKEND")
                .detailDomainName("api.leadme")
                .build();

        List<HostingResponse> hostingResponses = List.of(hostingResponse);

        DeploymentResponse deploymentResponse = DeploymentResponse.builder()
                .deploymentId(1L)
                .projectId(1L)
                .status("RUNNING")
                .serviceType("BACKEND")
                .repositoryName("repo-name")
                .repositoryUrl("https://github.com/repo-url")
                .repositoryLastCommitMessage("Initial commit")
                .repositoryLastCommitUserProfile("https://github.com/user/profile")
                .repositoryLastCommitUserName("userName")
                .repositoryOwner("sgo722")
                .build();

        List<DeploymentResponse> deploymentResponses = List.of(deploymentResponse);

        ProjectDetailResponse projectDetailResponse = ProjectDetailResponse.builder()
                .id(1L)
                .userId(1L)
                .projectName("project")
                .domainName("domain")
                .expirationDate("2024-10-11")
                .createdAt(fixedClock)
                .updatedAt(fixedClock)
                .webhookToken("githubWebhookToken")
                .build();

        projectDetailResponse.setDeployments(deploymentResponses);

        Long projectId = 1L;
        when(getProjectUseCase.getProject(eq(projectId)))
                .thenReturn(projectDetailResponse);

        // when
        ResultActions perform = mockMvc.perform(
                get("/v1/project/{projectId}",1)
                );

        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("프로젝트")
                                .summary("프로젝트 조회")
                                .description("사용자는 프로젝트를 조회할 수 있습니다.")
                                .pathParameters(
                                parameterWithName("projectId").description("프로젝트 ID")
                        )
                                .responseFields(
                                RestDocsUtils.combineFields(
                                        RestDocsUtils.getCommonResponseFields(),
                                        RestDocsUtils.getProjectResponseFields(),
                                        RestDocsUtils.getDeploymentsField()
                                )
                        ).build()
                )));

        // then
        perform.andDo(print());

    }

    @Test
    @DisplayName("프로젝트 페이징 조회")
    void 프로젝트_페이징_조회() throws Exception {

        // given
        Long userId = 1L;

        ProjectSearchResponse projectSearchResponse1 = ProjectSearchResponse.builder()
                .id(1L)
                .userId(1L)
                .projectName("project1")
                .domainName("domain1")
                .webhookToken("githubWebhookToken")
                .expirationDate("9999-12-31")
                .createdAt(fixedClock)
                .updatedAt(fixedClock)
                .build();

        ProjectSearchResponse projectSearchResponse2 = ProjectSearchResponse.builder()
                .id(1L)
                .userId(1L)
                .projectName("project2")
                .domainName("domain2")
                .expirationDate("9999-12-31")
                .webhookToken("githubWebhookToken")
                .createdAt(fixedClock)
                .updatedAt(fixedClock)
                .build();


        ProjectPageResponse projectPageResponse = ProjectPageResponse.builder()
                .content(Arrays.asList(projectSearchResponse1, projectSearchResponse2))
                .build();

        when(getProjectUseCase.getProjects(any(Pageable.class), any(String.class), eq(userId))).thenReturn(projectPageResponse);

        // when
        ResultActions perform = mockMvc.perform(
                get("/v1/project/search")
                        .param("page", "0")
                        .param("size", "9")
                        .param("sort", "createdAt,DESC")
                        .param("searchKeyword", "테스트")
                        .header("X-USER-ID", userId.toString())
                );
        
        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                        .tag("프로젝트")
                        .summary("프로젝트 페이징 조회")
                                .description("사용자는 프로젝트를 페이징 조회할 수 있습니다.")
//                                .pathParameters(
//                                        parameterWithName("page").description("조회할 페이지"),
//                                        parameterWithName("size").description("조회할 사이즈 개수"),
//                                        parameterWithName("sort").description("조회할 떄 정렬 기준"),
//                                        parameterWithName("searchKeyword").description("조회할 떄 검색 키워드")
//                                        )
                                .responseFields(
                                RestDocsUtils.combineFields(
                                        RestDocsUtils.getCommonResponseFields(),
                                        RestDocsUtils.getProjectPages()
                                ))
                                .build()
                        )
                ));
        // then
        perform.andDo(print());

    }

    @Test
    @DisplayName("프로젝트 수정")
    void updateProject() throws Exception {
        // given
        ProjectUpdateRequest projectUpdateRequest = ProjectUpdateRequest.builder()
                .projectName("updatedProject")
                .domainName("updatedDomain")
                .expirationDate("2024-11-11")
                .build();

        ProjectDetailResponse projectDetailResponse = ProjectDetailResponse
                .builder()
                .userId(1L)
                .id(1L)
                .projectName("updatedProject")
                .domainName("updatedDomain")
                .expirationDate("9999-12-31")
                .webhookToken("githubWebhookToken")
                .createdAt(fixedClock)
                .updatedAt(fixedClock)
                .build();

        when(updateProjectUseCase.updateProject(eq(1L), any(ProjectUpdateRequest.class))).thenReturn(projectDetailResponse);

        // when
        ResultActions perform = mockMvc.perform(
                patch("/v1/project/{projectId}", 1)
                        .header("X-USER-ID", 1L)
                        .content(toJson(projectUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("프로젝트")
                                .summary("프로젝트를 수정할 수 있습니다.")
                                .pathParameters(
                                        parameterWithName("projectId").description("프로젝트 ID")
                                )
                                .responseFields(
                                        RestDocsUtils.combineFields(
                                                RestDocsUtils.getCommonResponseFields(),
                                                RestDocsUtils.getProjectResponseFields(),
                                                new FieldDescriptor[]{
                                                        fieldWithPath("data.deployments").type(JsonFieldType.NULL).description("배포 목록")
                                                }
                                        )
                                ).build()
                )));

        // then
        perform.andDo(print());
    }

    @Test
    @DisplayName("프로젝트 삭제")
    void deleteProject() throws Exception {

        // given
        Long projectId = 1L;

        doNothing().when(deleteProjectUseCase).deleteProject(eq(projectId));

        // when
        ResultActions perform = mockMvc.perform(
                delete("/v1/project/{projectId}", projectId)
                        .contentType(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document((resource(
                        ResourceSnippetParameters.builder()
                                .tag("프로젝트")
                                .summary("프로젝트 삭제")
                                .description("사용자는 프로젝트를 삭제할 수 있습니다.")
                                .pathParameters(
                                parameterWithName("projectId").description("삭제할 프로젝트의 ID")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("message").type(JsonFieldType.NULL).optional().description("응답 메시지 (예: 'OK')"),
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드 (예: 200)"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터 (예: NULL)"))
                        .build()
                ))));

        // then
        perform.andDo(print());

    }

    @Test
    @DisplayName("중복되지 않는 도메인")
    void 중복되지_않는_도메인() throws Exception {
        // given
        DomainNameRequest domainNameRequest = new DomainNameRequest("domain");

        DomainNameResponse domainNameResponse = DomainNameResponse.builder()
                .message("생성할 수 있는 도메인입니다.")
                .canMake(true)
                .build();

        when(getProjectUseCase.isDuplicateDomainName(domainNameRequest)).thenReturn(false);

        // when
        ResultActions perform = mockMvc.perform(
                        post("/v1/project/domain/check")
                                .content(toJson(domainNameRequest))
                                .contentType(MediaType.APPLICATION_JSON));

        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("프로젝트")
                                .summary("중복되지 않는 도메인")
                                .description("도메인이 중복되지 않을 시 도메인 생성이 가능합니다.")
                                .requestFields(
                                    fieldWithPath("domainName").type(JsonFieldType.STRING).description("도메인명")
                                )
                                .responseFields(
                                RestDocsUtils.combineFields(
                                    RestDocsUtils.getCommonResponseFields(),
                                    new FieldDescriptor[] {
                                            fieldWithPath("data.message").type(JsonFieldType.STRING).description("메시지"),
                                            fieldWithPath("data.canMake").type(JsonFieldType.BOOLEAN).description("중복 여부")
                                    }
                                )
                                ).build()
                        )
                ));
    }

}