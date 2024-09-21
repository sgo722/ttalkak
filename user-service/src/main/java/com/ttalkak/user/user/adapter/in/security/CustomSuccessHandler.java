package com.ttalkak.user.user.adapter.in.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.ttalkak.user.common.util.CookieUtils;
import com.ttalkak.user.user.domain.CustomOAuth2User;
import com.ttalkak.user.user.domain.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

import static com.ttalkak.user.user.adapter.in.security.CustomAuthorizationRepository.REDIRECT_URI_PARAM_COOKIE;
import static com.ttalkak.user.user.adapter.in.security.JwtTokenProvider.REFRESH_TOKEN_COOKIE;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final CustomAuthorizationRepository authorizationRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.refresh-expire}")
    private int refreshExpire;

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE).map(Cookie::getValue);
        clearAuthenticationAttributes(request, response);
        return redirectUri.orElse(getDefaultTargetUrl());
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        log.debug("User {} logged in", customUserDetails.user());

        JwtToken jwtToken = jwtTokenProvider.generate(customUserDetails.user().getId(), customUserDetails.getAuthorities());

        String redirectURI = determineTargetUrl(request, response, authentication);
        CookieUtils.addCookie(response, REFRESH_TOKEN_COOKIE, jwtToken.getRefreshToken(), refreshExpire, true);
        getRedirectStrategy().sendRedirect(request, response, getRedirectUrl(redirectURI, jwtToken));
    }

    private String getRedirectUrl(String redirectURI, JwtToken token) {
        return UriComponentsBuilder.fromUriString(redirectURI)
                .queryParam("accessToken", token.getAccessToken())
                .queryParam("refreshToken", token.getRefreshToken())
                .build()
                .toUriString();

    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        authorizationRepository.removeAuthorizationRequest(request, response);
    }
}
