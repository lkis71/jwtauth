package com.security.jwt.dto;

import com.security.member.entity.TokenStorage;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class TokenStorageRequest {
    
    @NotNull
    private String refreshToken;
    
    @Setter
    private String ipAddress;

    @Builder
    public TokenStorageRequest(String refreshToken, String ipAddress) {
        this.refreshToken = refreshToken;
        this.ipAddress = ipAddress;
    }

    public TokenStorage toEntity() {
        return TokenStorage.builder()
                .refreshToken(this.refreshToken)
                .ipAddress(this.ipAddress)
                .build();
    }
}
