package com.security.member.service;

import com.security.dto.loginRequest;
import com.security.dto.TokenResponse;
import com.security.entity.Member;
import com.security.redis.RedisTemplateStore;
import com.security.repository.MemberRepository;
import com.security.service.JwtService;
import com.security.service.MemberService;
import com.security.util.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RedisTemplateStore redisStore;

    @Test
    void 로그인_성공() {
        // given
        String id = "test";
        String password = "1234";
        String encodedPassword = passwordEncoder.encode(password);

        loginRequest loginRequest = new loginRequest(id, password);
        Member mockMember = Member.builder()
                .id(id)
                .password(encodedPassword).build();

        Authentication mockAuthentication = mock(Authentication.class);
        TokenResponse mockTokenResponse = TokenResponse.builder()
                .tokenType("refreshToken")
                .accessToken("accessToken")
                .refreshToken("refreshToken").build();

        // mocking
        when(memberRepository.findById(id)).thenReturn(Optional.of(mockMember));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtService.authentication(id, encodedPassword)).thenReturn(mockAuthentication);
        when(jwtTokenProvider.createToken(mockAuthentication)).thenReturn(mockTokenResponse);

        // when
        TokenResponse result = memberService.login(loginRequest);

        // then
        assertEquals("accessToken", result.getAccessToken());
        assertEquals("refreshToken", result.getRefreshToken());
    }

    @Test
    void 로그인_성공_리프레시토큰_레디스저장_검증() {
        // given
        String id = "test";
        String password = "1234";
        String encodedPassword = passwordEncoder.encode(password);

        loginRequest loginRequest = new loginRequest(id, password);
        Member mockMember = Member.builder()
                .id(id)
                .password(encodedPassword).build();

        Authentication mockAuthentication = mock(Authentication.class);
        TokenResponse mockTokenResponse = TokenResponse.builder()
                .tokenType("refreshToken")
                .accessToken("accessToken")
                .refreshToken("refreshToken").build();

        // mocking
        when(memberRepository.findById(id)).thenReturn(Optional.of(mockMember));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtService.authentication(id, encodedPassword)).thenReturn(mockAuthentication);
        when(jwtTokenProvider.createToken(mockAuthentication)).thenReturn(mockTokenResponse);
        when(jwtTokenProvider.getExpirationMillis("refreshToken")).thenReturn(3600000L);

        // when
        memberService.login(loginRequest);

        // then
        verify(redisStore).set(
            eq("refreshToken:" + id),
            eq("refreshToken"),
            eq(3600000L)
        );
    }

    @Test
    void 로그인_사용자가_존재하지_않을_경우_예외() {
        // given
        String id = "test";
        String password = "1234";
        String encodedPassword = passwordEncoder.encode(password);

        loginRequest loginRequest = new loginRequest(id, password);

        // mocking
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        // when then
        assertThrows(IllegalArgumentException.class, () -> memberService.login(loginRequest));
    }

    @Test
    void 로그인_비밀번호_불일치_예외() {
        // given
        String id = "test";
        String password = "1234";
        String encodedPassword = passwordEncoder.encode(password);

        loginRequest loginRequest = new loginRequest(id, password);
        Member mockMember = Member.builder()
                .id(id)
                .password(encodedPassword).build();

        // mocking
        when(memberRepository.findById(id)).thenReturn(Optional.of(mockMember));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        // when then
        assertThrows(IllegalArgumentException.class, () -> memberService.login(loginRequest));
    }
}