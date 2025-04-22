package com.security.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequest {

    private String memberId;
    private String refreshToken;
}
