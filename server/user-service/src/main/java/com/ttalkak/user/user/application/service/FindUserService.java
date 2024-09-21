package com.ttalkak.user.user.application.service;

import com.ttalkak.user.common.UseCase;
import com.ttalkak.user.user.application.port.in.FindUserUseCase;
import com.ttalkak.user.user.application.port.out.LoadUserPort;
import com.ttalkak.user.user.domain.LoginUser;
import com.ttalkak.user.user.domain.User;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class FindUserService implements FindUserUseCase {
    private final LoadUserPort loadUserPort;

    @Override
    public LoginUser findUser(Long userId) {
        return loadUserPort.loadUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
