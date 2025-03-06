package com.jyp.service.my_today_service.service;

import com.jyp.service.my_today_service.model.Users;
import com.jyp.service.my_today_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users saveUser(Users user) {
        return userRepository.save(user);
    }


    public Optional<Users> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<Users> findUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
