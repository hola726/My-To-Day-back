package com.jyp.service.my_today_service.security;

import com.jyp.service.my_today_service.service.BlackListTokenService;
import com.jyp.service.my_today_service.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/// 인가 필터
/// 사용자가 무엇을 할 수있는지
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final BlackListTokenService blackListTokenService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserService userService, BlackListTokenService blackListTokenService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.blackListTokenService = blackListTokenService;
    }

    /// 토큰 검증 후 SecurityContext에 인증 정보 저장
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (blackListTokenService.isBlackListToken(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰이 무효화되었습니다");
                return;
            }
            if (jwtUtil.validateToken(token)) {
                String userId = jwtUtil.extractId(token);
                UserDetails user = userService.loadUserByUsername(userId);
                // 현재 사용자 권한이 정의되어 있지 않아 빈 Collection을 넣음
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user, null, Collections.emptyList());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
