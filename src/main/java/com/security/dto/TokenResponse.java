package com.security.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 클라이언트로 토큰 전송 DTO
 */
@Data
@Builder
public class TokenResponse {

    private String tokenType;
    private String accessToken;
    private String refreshToken;
}
