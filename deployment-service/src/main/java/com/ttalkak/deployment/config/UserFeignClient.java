package com.ttalkak.deployment.config;

import com.ttalkak.deployment.deployment.framework.useradapter.dto.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserFeignClient {

    @GetMapping("/feign/user/{userId}")
    UserInfoResponse getUserInfo(@PathVariable("userId")Long userId);
}
