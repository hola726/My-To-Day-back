package com.jyp.service.my_today_service.controller;

import com.jyp.service.my_today_service.model.Users;
import com.jyp.service.my_today_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        try {
            return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @GetMapping("/{userId}")
    public Optional<Users> getUserByUserId(@PathVariable String userId) {
        return userService.findUserByUserId(userId);
    }

    @GetMapping
    public Optional<Users> getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

}
