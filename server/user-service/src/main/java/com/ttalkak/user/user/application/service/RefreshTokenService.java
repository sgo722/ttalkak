package com.ttalkak.user.user.application.service;

import com.ttalkak.user.common.UseCase;
import com.ttalkak.user.user.adapter.in.security.JwtTokenProvider;
import com.ttalkak.user.user.application.port.in.RefreshTokenUseCase;
import com.ttalkak.user.user.domain.JwtToken;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenUseCase {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtToken refresh(String refreshToken) {
        return jwtTokenProvider.refreshToken(refreshToken);
    }
}
