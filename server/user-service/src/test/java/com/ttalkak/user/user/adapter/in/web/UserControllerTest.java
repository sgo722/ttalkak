package com.ttalkak.user.user.adapter.in.web;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ttalkak.user.common.RestDocsSupport;
import com.ttalkak.user.user.application.port.in.FindUserUseCase;
import com.ttalkak.user.user.domain.LoginUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 컨트롤러 테스트")
@WebMvcTest(
        value = UserController.class,
        properties = {
                "spring.cloud.config.enabled=false",
                "subdomain.endpoint=localhost:8000"
        }
)
class UserControllerTest extends RestDocsSupport {
    @MockBean
    private FindUserUseCase findUserUseCase;

    @Test
    @DisplayName("유저_정보_조회_테스트")
    void 유저_정보_조회_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        Long userId = 1L;

        LoginUser user = new LoginUser();
        user.setUserId(userId);
        user.setUsername("ttalkak-id");
        user.setEmail("ttalkak@ttalkak.com");
        user.setProfileImage("https://ttalkak.com/profile.jpg");
        user.setEmailVerified(true);
        user.setAccessToken("github-access-token");

        when(findUserUseCase.findUser(userId)).thenReturn(user);

        // * WHEN: 이걸 실행하면
        ResultActions perform = mockMvc.perform(get("/v1/user/me")
                        .header("X-USER-ID", userId)
        );

        // * THEN: 이런 결과가 나와야 한다
        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("유저")
                                .summary("유저 정보 조회 API")
                                .description("JWT 토큰 기반으로 유저 정보를 조회한다.")
                                .responseFields(response(
                                        fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                        fieldWithPath("data.username").type(JsonFieldType.STRING).description("사용자 이름"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("data.profileImage").type(JsonFieldType.STRING).description("프로필 이미지"),
                                        fieldWithPath("data.emailVerified").type(JsonFieldType.BOOLEAN).description("이메일 인증 여부"),
                                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰")
                                ))
                                .build()
                )));

    }
}