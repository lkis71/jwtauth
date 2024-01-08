package com.security.member.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class TokenStorage {

    @Id
    @Column(name = "member_id", nullable = false)
    private String id;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @Column(nullable = false)
    private String addressIp;

    @Builder
    public TokenStorage(String id, String refreshToken, String addressIp) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.addressIp = addressIp;
    }
}
