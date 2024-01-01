package com.security.member.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberLoginDto {

    private String id;
    private String password;
}
