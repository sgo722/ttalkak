package com.ttalkak.user.user.adapter.in.web.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SignUpRequest {
    @NotEmpty(message = "아이디를 입력해주세요.")
    @Length(min = 8, max = 30, message = "아이디는 8자 이상 30자 이하로 입력해주세요.")
    private String username;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Length(min = 8, max = 30, message = "비밀번호는 8자 이상 30자 이하로 입력해주세요.")
    private String password;

    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;
}