package com.ttalkak.user.user.application.service;

import com.ttalkak.user.common.UseCase;
import com.ttalkak.user.user.adapter.in.security.JwtTokenProvider;
import com.ttalkak.user.user.adapter.out.persistence.entity.UserEntity;
import com.ttalkak.user.user.application.port.in.AuthenticationUseCase;
import com.ttalkak.user.user.application.port.in.RegisterCommand;
import com.ttalkak.user.user.application.port.in.UserEmailVerifyUseCase;
import com.ttalkak.user.user.application.port.out.LoadUserPort;
import com.ttalkak.user.user.application.port.out.SaveUserPort;
import com.ttalkak.user.user.application.port.out.UserCreatePort;
import com.ttalkak.user.user.application.port.out.VerifyEmailUserPort;
import com.ttalkak.user.user.domain.JwtToken;
import com.ttalkak.user.user.domain.User;
import com.ttalkak.user.user.domain.UserCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService implements AuthenticationUseCase, UserEmailVerifyUseCase {
    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    private final VerifyEmailUserPort verifyEmailUserPort;
    private final UserCreatePort userCreatePort;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtToken signIn(String username, String password) {
        User user = loadUserPort.loadUser(username).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getUserRole().toString().toUpperCase())
        );

        return jwtTokenProvider.generate(user.getId(), authorities);
    }

    @Transactional
    @Override
    public void signUp(RegisterCommand command) {
        // 사용자 ID 중복 체크
        loadUserPort.loadUser(command.getUsername()).ifPresent(user -> {
            throw new IllegalStateException("이미 사용중인 사용자 ID 입니다.");
        });

        String encodedPassword = passwordEncoder.encode(command.getPassword());

        UserEntity entity = saveUserPort.save(command.getUsername(), encodedPassword, command.getEmail());

        // 사용자 생성 이벤트 발행
        userCreatePort.createUser(UserCreateEvent.of(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail()
        ));
    }

    @Transactional
    @Override
    public void verifyEmail(Long userId, String email) {
        loadUserPort.loadUser(userId).orElseThrow(
            () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );

        verifyEmailUserPort.verifyEmail(userId, email);
    }
}
