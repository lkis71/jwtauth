package com.security.member.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;

@Data
@NoArgsConstructor
public class MemberLoginDto {

    private String id;
    private String password;

    @Builder
    public MemberLoginDto(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
