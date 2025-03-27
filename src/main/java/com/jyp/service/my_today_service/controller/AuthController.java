package com.jyp.service.my_today_service.controller;

import com.jyp.service.my_today_service.dto.ApiResponse;
import com.jyp.service.my_today_service.dto.SignUpDto;
import com.jyp.service.my_today_service.dto.UserInfoDto;
import com.jyp.service.my_today_service.model.BlackListToken;
import com.jyp.service.my_today_service.model.Users;
import com.jyp.service.my_today_service.service.BlackListTokenService;
import com.jyp.service.my_today_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@Tag(name = "인증 API")
@RestController
public class AuthController {
    private final UserService userService;
    private final BlackListTokenService blackListTokenService;


    @Autowired
    public AuthController(UserService userService, BlackListTokenService blackListTokenService) {
        this.userService = userService;
        this.blackListTokenService = blackListTokenService;
    }

    @Operation(summary = "회원가입 API")
    @PostMapping("v1.0/sign-up")
    public ResponseEntity<ApiResponse<UserInfoDto>> SignUp(@Valid @RequestBody SignUpDto signUpDto) {
        final Users user = userService.saveUser(signUpDto.getUserId(), signUpDto.getPassword(), signUpDto.getEmail());
        final UserInfoDto userInfo = new UserInfoDto(user.getUserId(), user.getEmail());
        ApiResponse<UserInfoDto> response = new ApiResponse<>(true, "", "", userInfo);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "로그아웃 API")
    @PostMapping("v1.0/sign-out")
    public ResponseEntity<ApiResponse<String>> SignOut(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            LocalDateTime expiredDate = LocalDateTime.now();
            blackListTokenService.setBlackListTokenRepository(token, expiredDate);
            return ResponseEntity.ok(new ApiResponse<String>(true, "", "", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<String>(false, "", "", "{\"error\": \"유효한 토큰 필요\"}"));
    }
}
