package com.jyp.service.my_today_service.controller;

import com.jyp.service.my_today_service.dto.ApiResponse;
import com.jyp.service.my_today_service.dto.UserUpdateDto;
import com.jyp.service.my_today_service.dto.UserInfoDto;
import com.jyp.service.my_today_service.model.Users;
import com.jyp.service.my_today_service.security.AuthUtil;
import com.jyp.service.my_today_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "유저 API")
@RestController
public class UserController {
    private final UserService userService;
    private final AuthUtil authUtil;

    @Autowired
    public UserController(UserService userService, AuthUtil authUtil) {
        this.userService = userService;
        this.authUtil = authUtil;
    }

    @Operation(summary = "사용자 조회")
    @GetMapping("/v1.0/user")
    public ResponseEntity<ApiResponse<UserInfoDto>> getUserByUserId() {
        final Users user = authUtil.getUser();
        final UserInfoDto userInfoDto = new UserInfoDto(user.getUserId(), user.getEmail());
        ApiResponse<UserInfoDto> response = new ApiResponse<>(true, "", "", userInfoDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 삭제")
    @DeleteMapping("/v1.0/user")
    public ResponseEntity<ApiResponse<Boolean>> deleteUser() {
        final Users user = authUtil.getUser();
        userService.deleteUserById(user.getId());
        ApiResponse<Boolean> response = new ApiResponse<>(true, "", "", true);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "사용자 수정")
    @PutMapping("/v1.0/user")
    public ResponseEntity<ApiResponse<UserInfoDto>> updateUser(@RequestBody @Valid UserUpdateDto userUpdateDto) {
        final Users user = authUtil.getUser();
        userService.updateUser(user.getId(), userUpdateDto.getPassword(), userUpdateDto.getEmail());
        final UserInfoDto userInfo = new UserInfoDto(user.getUserId(), userUpdateDto.getEmail().isEmpty() ? user.getEmail() : userUpdateDto.getEmail().get());

        ApiResponse<UserInfoDto> response = new ApiResponse<>(true, "", "", userInfo);
        return ResponseEntity.ok(response);
    }

}
