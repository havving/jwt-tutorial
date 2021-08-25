package com.havving.tutorial.service;

import com.havving.tutorial.entity.User;
import com.havving.tutorial.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HAVVING
 * @since 2021-08-25
 */
@Component("userDetailService")
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {

        return userRepository.findOneWithAuthoritiesByUserName(username)
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    /**
     * 로그인 시, DB에서 유저 정보와 권한 정보 get
     * @return userdetails.User 객체 생성
     */
    private org.springframework.security.core.userdetails.User createUser(String username, User user) {
        if (!user.isActivated())
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");

        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getPassword(), grantedAuthorities);
    }
}
