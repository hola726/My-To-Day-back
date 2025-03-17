package com.jyp.service.my_today_service.service;

import com.jyp.service.my_today_service.model.Users;
import com.jyp.service.my_today_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users saveUser(String userId, String password, String email) {
        Users user = new Users();
        user.setUserId(userId);
        String encodePassword = passwordEncoder.encode(password);
        user.setPassword(encodePassword);
        user.setEmail(email);
        return userRepository.save(user);
    }

    @Transactional
    public Users updateUser(Long id, Optional<String> password, Optional<String> email) {
        Optional<Users> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        if (password.isPresent()) {
            String encodePassword = passwordEncoder.encode(password.get());
            user.get().setPassword(encodePassword);
        }
        email.ifPresent(user.get()::setEmail);

        return userRepository.save(user.get());
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

    // 인증용 userDetails 반환
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        final Optional<Users> user = userRepository.findByUserId(userId);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return User.withUsername(user.get().getUserId()).password(user.get().getPassword()).build();
    }
}
