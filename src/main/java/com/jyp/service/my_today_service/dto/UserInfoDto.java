package com.jyp.service.my_today_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

// 사용자 정보 dto
public class UserInfoDto {
    @Schema(description = "사용자 ID")
    private String userId;
    @Schema(description = "사용자 이메일")
    private String email;

    public UserInfoDto(String userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }


    public String getEmail() {
        return email;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
