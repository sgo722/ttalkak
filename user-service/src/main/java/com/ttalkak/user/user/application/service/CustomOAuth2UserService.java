package com.ttalkak.user.user.application.service;


import com.ttalkak.user.user.adapter.in.security.response.CustomOAuthUserFactory;
import com.ttalkak.user.user.adapter.in.security.response.OAuth2Response;
import com.ttalkak.user.user.adapter.out.persistence.entity.UserEntity;
import com.ttalkak.user.user.adapter.out.persistence.entity.UserEntityMapper;
import com.ttalkak.user.user.application.port.out.LoadUserPort;
import com.ttalkak.user.user.application.port.out.SaveUserPort;
import com.ttalkak.user.user.application.port.out.UserCreatePort;
import com.ttalkak.user.user.domain.CustomOAuth2User;
import com.ttalkak.user.user.domain.ProviderType;
import com.ttalkak.user.user.domain.User;
import com.ttalkak.user.user.domain.UserCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final LoadUserPort loadUserPort;
	private final SaveUserPort saveUserPort;
	private final UserCreatePort userCreatePort;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oauth2User = super.loadUser(userRequest);
		OAuth2AccessToken githubToken = userRequest.getAccessToken();

		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		ProviderType providerType = ProviderType.valueOf(registrationId.toUpperCase());
		OAuth2Response response = CustomOAuthUserFactory.parseOAuth2Response(providerType, oauth2User.getAttributes());

		String username = response.getName() + "_" + response.getProviderId();

		AtomicBoolean isNewUser = new AtomicBoolean(false);
		User user = loadUserPort.loadUser(username).orElseGet(() -> {
			log.info("신규 유저 생성 응답: {}", response);

			isNewUser.set(true);
			String encodedPassword = passwordEncoder.encode(response.getProviderId());
			UserEntity entity = saveUserPort.save(
					username,
					encodedPassword,
					response.getEmail(),
					response.getProviderId(),
					response.getProfileImage(),
					githubToken.getTokenValue()
			);
			return UserEntityMapper.toUser(entity);
		});

		if (StringUtils.hasText(githubToken.getTokenValue())) {
			saveUserPort.saveGithubToken(user.getUsername(), githubToken.getTokenValue());
		}

		log.debug("신규 사용자 여부: {}", isNewUser.get());
		if (isNewUser.get()) {
			userCreatePort.createUser(UserCreateEvent.of(
					user.getId(),
					user.getUsername(),
					user.getEmail(),
					""
			));
		}

		return new CustomOAuth2User(user);
	}
}
