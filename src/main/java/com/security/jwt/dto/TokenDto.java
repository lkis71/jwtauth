package com.security.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 클라이언트로 토큰 전송 DTO
 */
@Builder
@Data
@AllArgsConstructor
public class TokenDto {

    private String tokenType;
    private String accessToken;
    private String refreshToken;
}
