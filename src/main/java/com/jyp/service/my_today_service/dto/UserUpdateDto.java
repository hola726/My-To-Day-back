package com.jyp.service.my_today_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.Optional;

// 사용자 수정 dto
public class UserUpdateDto {
    @Schema(description = "사용자 비밀번호")
    @Size(min = 8, max = 13, message = "사용자 비밀번호는 최소 8자리 최대 13자리 입니다.")
    private String password;
    @Schema(description = "사용자 이메일")
    @Email(message = "유효한 이메일 형식이여야 합니다.")
    private String email;

    public UserUpdateDto(String userId, String password, String email) {
        this.password = password;
        this.email = email;
    }


    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setEmail(String email) {
        this.email = email;
    }
}
