package com.ttalkak.deployment.deployment.application.outputport;

import com.ttalkak.deployment.deployment.framework.useradapter.dto.UserInfoResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserOutputPort {

    UserInfoResponse getUserInfo(Long userId);
}
