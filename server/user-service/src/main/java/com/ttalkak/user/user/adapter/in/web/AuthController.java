package com.ttalkak.user.user.adapter.in.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.ttalkak.user.user.adapter.in.web.exception.CustomValidationException;
import com.ttalkak.user.common.WebAdapter;
import com.ttalkak.user.common.util.CookieUtils;
import com.ttalkak.user.user.adapter.in.web.request.SignInRequest;
import com.ttalkak.user.user.adapter.in.web.request.SignUpRequest;
import com.ttalkak.user.user.domain.ApiResponse;
import com.ttalkak.user.user.application.port.in.RefreshTokenUseCase;
import com.ttalkak.user.user.application.port.in.RegisterCommand;
import com.ttalkak.user.user.application.port.in.AuthenticationUseCase;
import com.ttalkak.user.user.domain.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ttalkak.user.user.adapter.in.security.JwtTokenProvider.REFRESH_TOKEN_COOKIE;

@Slf4j
@RestController
@WebAdapter
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
    private final AuthenticationUseCase authenticationUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @Value("${jwt.refresh-expire}")
    private int refreshExpire;

    @PostMapping("/sign-up")
    public ApiResponse<Void> signUp(
            @RequestBody @Validated SignUpRequest request,
            Errors errors
    ) {
        if (errors.hasErrors()) {
            throw new CustomValidationException("입력값이 올바르지 않습니다.", errors);
        }

        RegisterCommand command = new RegisterCommand(
                request.getUsername(),
                request.getPassword(),
                request.getEmail()
        );

        authenticationUseCase.signUp(command);
        return ApiResponse.success();
    }

    @PostMapping("/sign-in")
    public ApiResponse<JwtToken> loadUser(
            @RequestBody @Validated SignInRequest request,
            Errors errors,
            HttpServletResponse response
        ) {
        if (errors.hasErrors()) {
            throw new CustomValidationException("입력값이 올바르지 않습니다.", errors);
        }
        JwtToken token = authenticationUseCase.signIn(request.getUsername(), request.getPassword());
        CookieUtils.addCookie(response, REFRESH_TOKEN_COOKIE, token.getRefreshToken(), refreshExpire, true);
        return ApiResponse.success(token);
    }

    @PostMapping("/refresh")
    public ApiResponse<JwtToken> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Cookie cookie = CookieUtils.getCookie(request, REFRESH_TOKEN_COOKIE).orElseThrow(
                () -> new IllegalStateException("리프레시 토큰이 없습니다.")
        );

        JwtToken refresh = refreshTokenUseCase.refresh(cookie.getValue());
        
        CookieUtils.removeCookie(response, REFRESH_TOKEN_COOKIE);
        CookieUtils.addCookie(response, REFRESH_TOKEN_COOKIE, refresh.getRefreshToken(), refreshExpire, true);

        return ApiResponse.success(refresh);
    }
}
