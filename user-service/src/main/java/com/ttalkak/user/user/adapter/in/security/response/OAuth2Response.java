package com.ttalkak.user.user.adapter.in.security.response;

import com.ttalkak.user.user.domain.ProviderType;

public interface OAuth2Response {
	ProviderType getProvider();

	String getProviderId();

	String getEmail();

	String getName();

	String getProfileImage();
}
