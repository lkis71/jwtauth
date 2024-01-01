package com.security.config;

import com.security.jwt.JwtAuthenticationFilter;
import com.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * httpBasic().disable().csrf().disable() : httpBasic, csrf 속성을 사용하지 않음 (rest api 방식이므로)
     * sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) : 세션을 사용하지 않음 (jwt는 세션을 활용하지 않음)
     * antMatchers().permitAll() : 입력한 api의 모든 요청을 허가
     * antMatchers().hasRole("USER") : 사용자는 입력한 권한(hasRole)을 가져야 입력한 api로 요청이 가능
     * anyRequest().authenticated() : 이 밖에 모든 요청에 대해 인증을 필요로 함
     * addFilterBefore(customFilter, beforeFilter) : BeforeFilter 전에 customFilter를 실행 (JWT 사용하기 위해)
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/").hasRole("USER")
                    .anyRequest().authenticated()
                    .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                    .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
