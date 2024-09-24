package com.ttalkak.deployment.deployment.framework.useradapter;


import com.ttalkak.deployment.config.UserFeignClient;
import com.ttalkak.deployment.deployment.application.outputport.UserOutputPort;
import com.ttalkak.deployment.deployment.framework.useradapter.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAdapter implements UserOutputPort {

    private final UserFeignClient userFeignClient;

    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        return null;
    }
}
