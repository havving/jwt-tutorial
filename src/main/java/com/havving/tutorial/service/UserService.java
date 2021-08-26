package com.havving.tutorial.service;

import com.havving.tutorial.dto.UserDto;
import com.havving.tutorial.entity.Authority;
import com.havving.tutorial.entity.User;
import com.havving.tutorial.repository.UserRepository;
import com.havving.tutorial.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

/**
 * @author HAVVING
 * @since 2021-08-26
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User signUp(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUserName(userDto.getUserName()).orElse(null) != null)
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");

        // userName이 DB에 존재하지 않으면
        // 1) 권한 정보 생성 (ROLE_USER)
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        // 2) 유저 정보 생성
        User user = User.builder()
                .userName(userDto.getUserName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickName(userDto.getNickName())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        // DB에 정보 저장
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String userName) {
        return userRepository.findOneWithAuthoritiesByUserName(userName);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtil.getCurrentUserName().flatMap(userRepository::findOneWithAuthoritiesByUserName);
    }

}
