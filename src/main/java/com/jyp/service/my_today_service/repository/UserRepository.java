package com.jyp.service.my_today_service.repository;

import com.jyp.service.my_today_service.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
}
