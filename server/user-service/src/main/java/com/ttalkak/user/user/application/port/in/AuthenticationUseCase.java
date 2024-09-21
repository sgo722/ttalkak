package com.ttalkak.user.user.application.port.in;

import com.ttalkak.user.user.domain.JwtToken;

public interface AuthenticationUseCase {
    /**
     * 사용자가 입력한 아이디와 비밀번호로 인증 처리를 하고 JWT 토큰을 반환한다.
     *
     * @param username 사용자 ID
     * @param password 사용자 Password
     * @return {@link JwtToken} 인증에 사용될 JWT 토큰 
     */
    JwtToken signIn(String username, String password);

    /**
     * 사용자 등록 처리를 한다.
     *
     * @param command {@link RegisterCommand} 사용자 등록 정보
     */
    void signUp(RegisterCommand command);
}
