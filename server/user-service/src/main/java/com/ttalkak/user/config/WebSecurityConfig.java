package com.ttalkak.user.config;

import com.ttalkak.user.user.adapter.in.security.CustomAuthorizationRepository;
import com.ttalkak.user.user.adapter.in.security.CustomSuccessHandler;
import com.ttalkak.user.user.application.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final CustomAuthorizationRepository authorizationRepository;
    private final CustomOAuth2UserService oAuth2UserService;
    private final CustomSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain configure(HttpSecurity security) throws Exception {
        return security
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth.anyRequest().permitAll())
                .oauth2Login((oauth2) -> oauth2.authorizationEndpoint(authorization ->
                            authorization.baseUri("/*/auth/authorization")
                                    .authorizationRequestRepository(authorizationRepository)
                        ).redirectionEndpoint(redirect -> redirect.baseUri("/v1/auth/oauth2/code/*"))
                        .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                        .successHandler(successHandler)
                )
                .build();
    }
}
