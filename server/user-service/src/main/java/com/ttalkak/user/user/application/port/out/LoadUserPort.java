package com.ttalkak.user.user.application.port.out;

import com.ttalkak.user.user.domain.LoginUser;
import com.ttalkak.user.user.domain.User;

import java.util.Optional;

public interface LoadUserPort {
    /**
     * 사용자 ID를 이용하여 사용자 정보를 조회한다.
     *
     * @param userId 사용자 ID
     * @return {@link LoginUser} 사용자 정보
     */
    Optional<LoginUser> loadUser(Long userId);

    /**
     * 사용자 username 을 이용하여 사용자 정보를 조회한다.
     *
     * @param username 사용자 username
     * @return {@link User} 사용자 정보
     */
    Optional<User> loadUser(String username);
}
