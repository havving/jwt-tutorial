package com.havving.tutorial.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * JWT를 위한 커스텀 필터
 *
 * @author HAVVING
 * @since 2021-08-24
 */
public class JwtFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * 실제 필터링 로직
     * 토큰의 인증정보를 SecurityContext에 저장하는 역할
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // request에서 토큰 get
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        // 토큰 유효성 검증
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            // 정상 토큰이면 SecurityContext에 저장
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Security Context에 '{}' 인증 정보를 젖아했습니다, uri: {}", authentication.getName(), requestURI);
        } else log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);

        chain.doFilter(request, response);
    }

    /**
     * Request Header에서 토큰 정보를 꺼내오기 위한 역할
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);

        return null;
    }


}
