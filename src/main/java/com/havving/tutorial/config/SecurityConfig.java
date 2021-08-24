package com.havving.tutorial.config;

import com.havving.tutorial.jwt.JwtAccessDeniedHandler;
import com.havving.tutorial.jwt.JwtAuthenticationEntryPoint;
import com.havving.tutorial.jwt.JwtSecurityConfig;
import com.havving.tutorial.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author HAVVING
 * @since 2021-08-23
 */

/**
 * @EnableWebSecurity : 기본적인 WEB 보안 활성화
 * [추가적인 설정 방법]
 * 1) WebSecurityConfigurer Interface implements
 * 2) WebSecurityConfigurerAdapter abstract class extends
 *
 * @EnableGlobalMethodSecurity : @PreAuthorize를 메서드 단위로 추가
 **/
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // 컴포넌트 주입
    public SecurityConfig(TokenProvider tokenProvider, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 토큰을 사용하기 때문에 csrf 설정 disable

                .exceptionHandling()  // Exception을 핸들링할 때 아래의 클래스로 추가
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console을 위한 설정
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 세션 설정 stateless
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()  // HttpServletRequest를 사용하는 요청들에 대한 접근 제한을 설정
                .antMatchers("/api/hello").permitAll()  // url에 대한 요청은 인증없이 접근을 허용한다
                .antMatchers("/api/authenticate").permitAll()  // 로그인 API는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll
                .antMatchers("/api/signup").permitAll()   // 회원가입 API는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll
                .anyRequest().authenticated()  // 그 외 나머지 요청들은 모두 인증되어야 한다

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
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
