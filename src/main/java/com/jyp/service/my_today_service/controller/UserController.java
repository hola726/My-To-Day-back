package com.jyp.service.my_today_service.controller;

import com.jyp.service.my_today_service.dto.ApiResponse;
import com.jyp.service.my_today_service.dto.UserUpdateDto;
import com.jyp.service.my_today_service.dto.UserInfoDto;
import com.jyp.service.my_today_service.model.Users;
import com.jyp.service.my_today_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "유저 API")
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "사용자 조회(사용자 ID)")
    @GetMapping("/v1.0/user/{userId}")
    public ResponseEntity<ApiResponse<UserInfoDto>> getUserByUserId(@PathVariable String userId) {
        final Optional<Users> user = userService.findUserByUserId(userId);
        if (user.isEmpty()) {
            ApiResponse<UserInfoDto> errorResponse = new ApiResponse<>(false, "USER_NOT_FOUND", "User with ID " + userId + " not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // 404
        }
        final UserInfoDto userInfoDto = new UserInfoDto(user.get().getUserId(), user.get().getEmail());
        ApiResponse<UserInfoDto> response = new ApiResponse<>(true, "", "", userInfoDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 삭제")
    @DeleteMapping("/v1.0/user/{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteUser(@PathVariable Long id) {
        final Optional<Users> user = userService.findUserById(id);
        if (user.isEmpty()) {
            ApiResponse<Boolean> errorResponse = new ApiResponse<>(false, "USER_NOT_FOUND", "UserId " + id + " not found", false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        userService.deleteUserById(id);
        ApiResponse<Boolean> response = new ApiResponse<>(true, "", "", true);
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "사용자 수정")
    @PutMapping("/v1.0/user/{id}")
    public ResponseEntity<ApiResponse<UserInfoDto>> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDto userUpdateDto) {
        final Optional<Users> user = userService.findUserById(id);
        if (user.isEmpty()) {
            ApiResponse<UserInfoDto> errorResponse = new ApiResponse<>(false, "USER_NOT_FOUND", "UserId " + id + " not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
        userService.updateUser(id, userUpdateDto.getPassword(), userUpdateDto.getEmail());
        final UserInfoDto userInfo = new UserInfoDto(user.get().getUserId(), userUpdateDto.getEmail().isEmpty() ? user.get().getEmail() : userUpdateDto.getEmail().get());

        ApiResponse<UserInfoDto> response = new ApiResponse<>(true, "", "", userInfo);
        return ResponseEntity.ok(response);
    }

}
