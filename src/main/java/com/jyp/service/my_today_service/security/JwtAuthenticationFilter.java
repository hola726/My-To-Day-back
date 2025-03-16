package com.jyp.service.my_today_service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyp.service.my_today_service.dto.SignInDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

/// 로그인시 사용되는 필터
/// 인증 확인
/// 사용자가 자신이 누구인지 신원 확인
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        // 로그인 api 필터링
        setFilterProcessesUrl("/*/sign-in");
    }

    // 로그인 시도시 사용자 자격증명을 추출하고 인증 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 요청 본문에서 username, password 파싱
            ObjectMapper mapper = new ObjectMapper();
            SignInDto loginRequest = mapper.readValue(request.getInputStream(), SignInDto.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    loginRequest.getUserId(), loginRequest.getPassword());
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 인증이 성공했을 때 호출되어 JWT를 생성하여 응답 헤더에 추가
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        String userId = authResult.getName();
        String token = jwtUtil.generateToken(userId);

        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
    }
}
