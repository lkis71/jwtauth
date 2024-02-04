package com.security.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginRequest {

    private String id;
    private String password;
}
