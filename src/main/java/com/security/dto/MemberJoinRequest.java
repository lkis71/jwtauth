package com.security.dto;

import com.security.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberJoinRequest {

    private String id;
    private String password;
    private String role;

    public Member toEntity() {
        return Member.builder()
                .id(this.id)
                .password(this.password)
                .role(this.role)
                .build();
    }
}
