package com.jyp.service.my_today_service.controller;

import com.jyp.service.my_today_service.dto.ApiResponse;
import com.jyp.service.my_today_service.dto.SignUpDto;
import com.jyp.service.my_today_service.dto.UserInfoDto;
import com.jyp.service.my_today_service.model.Users;
import com.jyp.service.my_today_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "인증 API")
@RestController
public class AuthController {
    private UserService userService;


    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "회원가입 API")
    @PostMapping("v1.0/sign-up")
    public ResponseEntity<ApiResponse<UserInfoDto>> SignUp(@Valid @RequestBody SignUpDto signUpDto) {
        final Users user = userService.saveUser(signUpDto.getUserId(), signUpDto.getPassword(), signUpDto.getEmail());
        final UserInfoDto userInfo = new UserInfoDto(user.getUserId(), user.getEmail());
        ApiResponse<UserInfoDto> response = new ApiResponse<>(true, "", "", userInfo);
        return ResponseEntity.ok(response);
    }
}
