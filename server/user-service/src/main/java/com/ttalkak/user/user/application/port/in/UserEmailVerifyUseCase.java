package com.ttalkak.user.user.application.port.in;

public interface UserEmailVerifyUseCase {
    /**
     * 이메일 인증 처리
     *
     * @param userId 사용자 ID
     * @param email 이메일
     */
    void verifyEmail(Long userId, String email);
}
