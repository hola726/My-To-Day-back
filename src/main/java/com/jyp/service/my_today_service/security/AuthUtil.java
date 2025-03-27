package com.jyp.service.my_today_service.security;

import com.jyp.service.my_today_service.model.Users;
import com.jyp.service.my_today_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    private final UserService userService;

    @Autowired
    public AuthUtil(UserService userService) {
        this.userService = userService;
    }

    ///  유저 정보 가져오기
    public Users getUser() {
        // 현재 인증된 사용자 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userIdFromToken;

        if (principal instanceof UserDetails) {
            userIdFromToken = ((UserDetails) principal).getUsername(); // JWT에서 username 또는 userId로 설정된 값
        } else {
            throw new RuntimeException("인증된 사용자를 찾을 수 없습니다.");
        }

        // UserService를 통해 Users 엔티티 조회
        return userService.findUserByUserId(userIdFromToken)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + userIdFromToken));
    }
}
