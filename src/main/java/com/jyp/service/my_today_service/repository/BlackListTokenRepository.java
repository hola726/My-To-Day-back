package com.jyp.service.my_today_service.repository;

import com.jyp.service.my_today_service.model.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListTokenRepository extends JpaRepository<BlackListToken, Long> {
    boolean existsByToken(String token);
}
