package com.ttalkak.project.project.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;

@TestConfiguration
public class RestDocsConfig {
    @Bean
    public RestDocumentationResultHandler restDocumentationResultHandler() {
        return document("{class-name}/{method-name}",
                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
        );
    }
}
