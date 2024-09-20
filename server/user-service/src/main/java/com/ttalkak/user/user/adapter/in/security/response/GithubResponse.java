package com.ttalkak.user.user.adapter.in.security.response;


import com.ttalkak.user.user.domain.ProviderType;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GithubResponse implements OAuth2Response {
	private final Map<String, Object> attribute;

	public GithubResponse(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	@Override
	public ProviderType getProvider() {
		return ProviderType.GITHUB;
	}

	@Override
	public String getProviderId() {
		return attribute.get("node_id").toString();
	}

	@Override
	public String getEmail() {
		Object email = attribute.get("email");
		if (email == null) {
			return null; 
		}
		return email.toString();
	}

	@Override
	public String getName() {
		Object name = attribute.get("name");
		if (name == null) {
			return getProviderId();
		}
		return name.toString();
	}

	@Override
	public String getProfileImage() {
		Object avatarUrl = attribute.get("avatar_url");
		if (avatarUrl == null) {
			return "";
		}
		return avatarUrl.toString();
	}
}
