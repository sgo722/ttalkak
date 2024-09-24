package com.ttalkak.user.user.adapter.in.web;

import com.ttalkak.user.common.WebAdapter;
import com.ttalkak.user.user.domain.ApiResponse;
import com.ttalkak.user.user.application.port.in.FindUserUseCase;
import com.ttalkak.user.user.domain.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {
    private final FindUserUseCase findUserUseCase;

    @GetMapping("/me")
    public ApiResponse<LoginUser> findUser(@RequestHeader("X-USER-ID") Long userId) {
        log.debug("User {} requested user information", userId);
        
        return ApiResponse.success(findUserUseCase.findUser(userId));
    }
}
