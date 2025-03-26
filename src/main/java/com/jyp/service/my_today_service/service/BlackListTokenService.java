package com.jyp.service.my_today_service.service;

import com.jyp.service.my_today_service.model.BlackListToken;
import com.jyp.service.my_today_service.repository.BlackListTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BlackListTokenService {
    private final BlackListTokenRepository blackListTokenRepository;

    @Autowired
    public BlackListTokenService(BlackListTokenRepository blackListTokenRepository) {
        this.blackListTokenRepository = blackListTokenRepository;
    }

    public void setBlackListTokenRepository(String token, LocalDateTime expiredDate) {
        blackListTokenRepository.save(new BlackListToken(token, expiredDate));
    }

    public boolean isBlackListToken(String token) {
        return blackListTokenRepository.existsByToken(token);
    }
}
