package com.havving.tutorial.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * @author HAVVING
 * @since 2021-08-26
 */
public class SecurityUtil {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtil.class);

    public SecurityUtil() {
    }

    /**
     * SecurityContext의 Authentication 객체를 이용해 userName return
     * @return userName
     */
    public static Optional<String> getCurrentUserName() {
        // JwtFilter의 doFilter 메서드에서 request가 들어올 때 SecurityContext에 Authentication 객체가 저장됨
        // 이 저장된 객체를 가져와 사용
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("Security Context에 인증 정보가 없습니다.");
            return Optional.empty();
        }

        String userName = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            userName = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String)
            userName = (String) authentication.getPrincipal();

        return Optional.ofNullable(userName);
    }

}
