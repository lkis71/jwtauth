package com.security.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenStorageRequest {
    
    @NotNull
    private String refreshToken;

    @NotNull
    private String memberId;
}
