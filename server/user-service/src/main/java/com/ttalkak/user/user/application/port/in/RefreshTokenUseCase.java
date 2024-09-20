package com.ttalkak.user.user.application.port.in;

import com.ttalkak.user.user.domain.JwtToken;

public interface RefreshTokenUseCase {
    /**
     * 리프레시 토큰을 이용하여 새로운 JWT 토큰을 발급한다.
     *
     * @param refreshToken 리프레시 토큰
     * @return {@link JwtToken} 새로운 JWT 토큰
     */
    JwtToken refresh(String refreshToken);
}
