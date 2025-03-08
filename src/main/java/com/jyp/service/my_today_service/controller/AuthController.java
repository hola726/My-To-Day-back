package com.jyp.service.my_today_service.controller;

import com.jyp.service.my_today_service.dto.ApiResponse;
import com.jyp.service.my_today_service.dto.SignUpDto;
import com.jyp.service.my_today_service.model.Users;
import com.jyp.service.my_today_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


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
    public ResponseEntity<ApiResponse<SignUpDto>> SignUp(@Valid @RequestBody SignUpDto signUpDto) {
        userService.saveUser(new Users(signUpDto.getUserId(), signUpDto.getPassword(), signUpDto.getEmail()));
        ApiResponse<SignUpDto> response = new ApiResponse<SignUpDto>(true, "", "", signUpDto);
        return ResponseEntity.ok(response);
    }
}
