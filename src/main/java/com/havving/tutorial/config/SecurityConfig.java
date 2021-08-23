package com.havving.tutorial.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author HAVVING
 * @since 2021-08-23
 */

/**
 * @EnableWebSecurity : 기본적인 WEB 보안 활성화
 * [추가적인 설정 방법]
 * 1) WebSecurityConfigurer Interface implements
 * 2) WebSecurityConfigurerAdapter abstract class extends
 **/
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * authorizeRequests() : HttpServletRequest를 사용하는 요청들에 대한 접근 제한을 설정
     * antMatchers("/api/hello").permitAll() : url에 대한 요청은 인증없이 접근을 허용한다
     * anyRequest().authenticated() : 나머지 요청들은 모두 인증되어야 한다
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/hello").permitAll()
                .anyRequest().authenticated();
    }

    /**
     * h2-console 하위 모든 요청들과 파비콘 관련 요청은 Spring Security 로직을 수행하지 않도록 접근 제한 설정
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/h2-console/**",
                        "/favicon.ico");
    }
}
