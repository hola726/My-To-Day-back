package com.jyp.service.my_today_service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class BlackListToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String token;
    @Column(nullable = false)
    private LocalDateTime expiredDate;

    public BlackListToken() {
    }

    public BlackListToken(String token, LocalDateTime expiredDate) {
        this.token = token;
        this.expiredDate = expiredDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }
}
