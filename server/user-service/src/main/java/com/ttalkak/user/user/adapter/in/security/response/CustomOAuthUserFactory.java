package com.ttalkak.user.user.adapter.in.security.response;

import com.ttalkak.user.user.domain.ProviderType;

import java.util.Map;

public class CustomOAuthUserFactory {
    public static OAuth2Response parseOAuth2Response(ProviderType providerType, Map<String, Object> attributes) {
        return switch (providerType) {
            case GITHUB -> new GithubResponse(attributes);
        };
    }
}
