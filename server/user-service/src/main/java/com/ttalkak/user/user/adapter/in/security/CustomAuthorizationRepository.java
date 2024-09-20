package com.ttalkak.user.user.adapter.in.security;

import com.ttalkak.user.common.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CustomAuthorizationRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private static final int COOKIE_EXPIRE_TIME = 60 * 60;
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE = "oauth_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE = "oauth_redirect_uri";

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtils.removeCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE);
            CookieUtils.removeCookie(response, REDIRECT_URI_PARAM_COOKIE);
            return;
        }

        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE, CookieUtils.serialize(authorizationRequest), COOKIE_EXPIRE_TIME, false);

        String redirectURI = request.getParameter(REDIRECT_URI_PARAM_COOKIE);

        if (StringUtils.hasText(redirectURI)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE, redirectURI, COOKIE_EXPIRE_TIME, false);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }
}
