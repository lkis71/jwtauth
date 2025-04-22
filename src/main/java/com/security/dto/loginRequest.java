package com.security.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class loginRequest {

    private String id;
    private String password;
}
