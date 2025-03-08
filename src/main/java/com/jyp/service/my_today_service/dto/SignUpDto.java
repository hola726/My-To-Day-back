package com.jyp.service.my_today_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SignUpDto {
    @Schema(description = "사용자 ID")
    @NotEmpty(message = "사용자 ID는 필수 입니다.")
    @Size(max = 16, message = "사용자 ID의 길이는 최대 16자리 입니다.")
    private String userId;
    @Schema(description = "사용자 비밀번호")
    @NotEmpty(message = "사용자 비밀번호는 필수 입니다.")
    @Size(min = 8, max = 13, message = "사용자 비밀번호는 최소 8자리 최대 13자리 입니다.")
    private String password;
    @Schema(description = "사용자 이메일")
    @NotEmpty(message = "사용자 이메일은 필수 입니다.")
    @Email(message = "유효한 이메일 형식이여야 합니다.")
    private String email;

    public SignUpDto(String userId, String password, String email) {
        this.userId = userId;
        this.password = password;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
