package com.security.config;

import com.security.filter.JwtTokenFilter;
import com.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
     * httpBasic().disable() : 기본 http의 ID/PW의 로그인 방식을 사용하지 않는다는 설정
     * csrf().disable() : csrf 속성을 사용하지 않음 (rest api 방식이므로)
     * sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) : 세션을 사용하지 않음 (jwt는 세션을 활용하지 않음)
     * antMatchers().permitAll() : 입력한 api의 모든 요청을 허가
     * antMatchers().authenticated() : 모든 POST 요청은 인증 필요
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
                    .antMatchers("/api/v1/member/join", "/api/v1/member/login", "/api/v1/member/token/refresh").permitAll()
//                    .requestMatchers(PathRequest.toH2Console()).permitAll()
                    .antMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
                    .anyRequest().authenticated()
                    .and()
                    .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                    .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
