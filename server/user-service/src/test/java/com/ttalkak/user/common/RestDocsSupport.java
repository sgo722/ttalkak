package com.ttalkak.user.common;

import com.nimbusds.jose.shaded.gson.Gson;
import com.ttalkak.user.config.RestDocsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@Disabled
@ExtendWith({RestDocumentationExtension.class})
@Import(RestDocsConfig.class)
@AutoConfigureRestDocs
public abstract class RestDocsSupport {
    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected MockMvc mockMvc;

    private final Gson gson = new Gson();

    @BeforeEach
    public void setUp(WebApplicationContext applicationContext, RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    protected FieldDescriptor[] response(FieldDescriptor... descriptor) {
        return Stream.concat(Arrays.stream(new FieldDescriptor[] {
                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
        }), Stream.of(descriptor)).toArray(FieldDescriptor[]::new);
    }

    protected FieldDescriptor[] response(FieldDescriptor descriptor) {
        return new FieldDescriptor[] {
                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                descriptor
        };
    }

    protected FieldDescriptor[] empty() {
        return response(fieldWithPath("data").type(JsonFieldType.NULL).description("빈 데이터"));
    }

    protected <T> String toJson(T data) {
        return gson.toJson(data);
    }

    protected <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
