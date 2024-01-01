package com.security.member.dto;

import com.security.member.entity.Member;
import lombok.*;

@Data
@NoArgsConstructor
public class MemberJoinDto {

    private String id;
    private String password;
    private String role;

    @Builder
    public MemberJoinDto(String id, String password, String role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    public Member toEntity() {
        return Member.builder()
                .id(this.id)
                .password(this.password)
                .role(this.role)
                .build();
    }
}
