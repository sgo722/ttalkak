package com.ttalkak.user.user.application.port.in;

import com.ttalkak.user.user.domain.LoginUser;

public interface FindUserUseCase {
    /**
     * 사용자 ID를 이용하여 사용자 정보를 조회한다.
     *
     * @param userId 사용자 ID
     * @return {@link LoginUser} 사용자 정보
     */
    LoginUser findUser(Long userId);
}
