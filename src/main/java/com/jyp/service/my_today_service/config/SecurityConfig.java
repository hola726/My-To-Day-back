package com.jyp.service.my_today_service.config;

import com.jyp.service.my_today_service.security.JwtAuthenticationFilter;
import com.jyp.service.my_today_service.security.JwtAuthorizationFilter;
import com.jyp.service.my_today_service.security.JwtUtil;
import com.jyp.service.my_today_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Spring 보안설정
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserService userService;


    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        // 토큰 기반이므로 csrf 비활성화
        http.csrf(AbstractHttpConfigurer::disable)
                // REST API로 세션 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // 로그인, 회원가입 api 인증 없음
                        .requestMatchers("*/sign-in", "*/sign-up").permitAll()
                        // swagger 경로 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger").permitAll()
                        // H2 Console 경로 허용
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtUtil))    // 로그인 필터
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil, userService),
                        UsernamePasswordAuthenticationFilter.class) // 토큰 검증 필터
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
                // H2 Console은 iframe을 사용해 렌더링, 동일 출처의 iframe에서 로드되게 허용
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
