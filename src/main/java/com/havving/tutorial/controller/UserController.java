package com.havving.tutorial.controller;

import com.havving.tutorial.dto.UserDto;
import com.havving.tutorial.entity.User;
import com.havving.tutorial.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author HAVVING
 * @since 2021-08-26
 */
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.signUp(userDto));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // USER, ADMIN 권한 허용
    public ResponseEntity<User> getMyUserInfo() {
        return ResponseEntity.ok(userService.getUserWithAuthorities().get());
    }

    @GetMapping("/user/{userName}")
    @PreAuthorize("hasAnyRole('ADMIN')")  // ADMIN 권한만 허용
    public ResponseEntity<User> getUserInfo(@PathVariable String userName) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(userName).get());
    }
}
