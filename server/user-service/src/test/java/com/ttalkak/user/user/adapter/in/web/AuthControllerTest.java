package com.ttalkak.user.user.adapter.in.web;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.ttalkak.user.common.RestDocsSupport;
import com.ttalkak.user.user.adapter.in.web.request.SignInRequest;
import com.ttalkak.user.user.adapter.in.web.request.SignUpRequest;
import com.ttalkak.user.user.application.port.in.AuthenticationUseCase;
import com.ttalkak.user.user.application.port.in.RefreshTokenUseCase;
import com.ttalkak.user.user.application.port.in.RegisterCommand;
import com.ttalkak.user.user.domain.JwtToken;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("인증 컨트롤러 테스트")
@WebMvcTest(
        value = AuthController.class,
        properties = {
                "spring.cloud.config.enabled=false",
                "subdomain.endpoint=localhost:8000"
        }
)
class AuthControllerTest extends RestDocsSupport {
    @MockBean
    private AuthenticationUseCase authenticationUseCase;

    @MockBean
    private RefreshTokenUseCase refreshTokenUseCase;

    @Test
    @DisplayName("회원가입_성공_테스트")
    void 회원가입_성공_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        SignUpRequest request = new SignUpRequest();
        request.setUsername("ttalkak-id");
        request.setPassword("ttalkak123!");
        request.setEmail("ttalkak@ttalkak.com");
        request.setAddress("0x1234567890");

        RegisterCommand command = new RegisterCommand(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getAddress()
        );

        // * WHEN: 이걸 실행하면
        ResultActions perform = mockMvc.perform(post("/v1/auth/sign-up")
                .header("X-USER-ID", 1L)
                .content(toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // * THEN: 이런 결과가 나와야 한다
        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증")
                                .summary("회원가입 API")
                                .description("아이디, 비밀번호, 이메일을 입력받아 회원가입을 진행합니다.")
                                .requestFields(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일"),
                                        fieldWithPath("address").type(JsonFieldType.STRING).description("사용자 지갑 주소")
                                ).responseFields(empty())
                                .build()
                )));

        verify(authenticationUseCase).signUp(command);
    }

    @Test
    @DisplayName("회원가입_실패_테스트")
    void 회원가입_실패_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        SignUpRequest request = new SignUpRequest();
        request.setUsername("id");
        request.setPassword("pass");
        request.setEmail("email");

        // * WHEN: 이걸 실행하면
        ResultActions perform = mockMvc.perform(post("/v1/auth/sign-up")
                .header("X-USER-ID", 1L)
                .content(toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // * THEN: 이런 결과가 나와야 한다
        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증")
                                .summary("회원가입 API")
                                .description("아이디, 비밀번호, 이메일을 입력받아 회원가입을 진행합니다.")
                                .requestFields(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("사용자 이메일")
                                ).responseFields(response(
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일 에러 메시지"),
                                        fieldWithPath("data.username").type(JsonFieldType.STRING).description("아이디 에러 메시지"),
                                        fieldWithPath("data.password").type(JsonFieldType.STRING).description("비밀번호 에러 메시지")
                                ))
                                .build()
                )));
    }

    @Test
    @DisplayName("로그인_성공_테스트")
    void 로그인_성공_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        SignInRequest request = new SignInRequest();
        request.setUsername("ttalkak-id");
        request.setPassword("ttalkak");

        JwtToken token = new JwtToken("accessToken", "refreshToken");

        when(authenticationUseCase.signIn(request.getUsername(), request.getPassword())).thenReturn(token);

        // * WHEN: 이걸 실행하면
        ResultActions perform = mockMvc.perform(post("/v1/auth/sign-in")
                .header("X-USER-ID", 1L)
                .content(toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        );

        // * THEN: 이런 결과가 나와야 한다
        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증")
                                .summary("로그인 API")
                                .description("아이디, 비밀번호를 입력받아 로그인을 진행합니다.")
                                .requestFields(
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("사용자 아이디"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("사용자 비밀번호")
                                ).responseHeaders(List.of(
                                        headerWithName("Set-Cookie").description("리프레시 토큰 쿠키")
                                ))
                                .responseFields(response(
                                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                ))
                                .build()
                )));

    }

    @Test
    @DisplayName("리프레시_성공_테스트")
    void 리프레시_성공_테스트() throws Exception {
        // * GIVEN: 이런게 주어졌을 때
        JwtToken token = new JwtToken("accessToken", "refreshToken");

        when(refreshTokenUseCase.refresh("refreshToken")).thenReturn(token);

        // * WHEN: 이걸 실행하면
        ResultActions perform = mockMvc.perform(post("/v1/auth/refresh")
                .header("X-USER-ID", 1L)
                .cookie(new Cookie("refreshToken", "refreshToken"))
        );

        // * THEN: 이런 결과가 나와야 한다
        perform.andExpect(status().isOk())
                .andDo(this.restDocs.document(resource(
                        ResourceSnippetParameters.builder()
                                .tag("인증")
                                .summary("리프레시 API")
                                .description("리프레시 토큰을 이용해 액세스 토큰을 재발급합니다.")
                                .responseFields(response(
                                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                        fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰")
                                ))
                                .build()
                )));
    }
}