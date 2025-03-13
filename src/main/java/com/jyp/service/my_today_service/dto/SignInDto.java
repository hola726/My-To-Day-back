package com.jyp.service.my_today_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

// 사용자 로그인 dto
public class SignInDto {
    @Schema(description = "사용자 ID")
    @NotEmpty(message = "사용자 ID는 필수 입니다.")
    @Size(max = 16, message = "사용자 ID의 길이는 최대 16자리 입니다.")
    private String userId;
    @Schema(description = "사용자 비밀번호")
    @NotEmpty(message = "사용자 비밀번호는 필수 입니다.")
    @Size(min = 8, max = 13, message = "사용자 비밀번호는 최소 8자리 최대 13자리 입니다.")
    private String password;

    public SignInDto() {
    }

    public SignInDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
