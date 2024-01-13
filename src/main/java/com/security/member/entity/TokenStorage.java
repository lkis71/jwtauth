package com.security.member.entity;

import com.security.jwt.dto.TokenStorageRequest;
import com.security.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenStorage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Setter
    private String refreshToken;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memer_id")
    @Column(nullable = false)
    private Member member;

    @Builder
    public TokenStorage(String refreshToken, Member member) {
        this.refreshToken = refreshToken;
        this.member = member;
    }

    public Boolean isValidToken(TokenStorageRequest tokenStorageRequest) {
        return this.refreshToken.equals(tokenStorageRequest.getRefreshToken())
                && this.member.getId().equals(tokenStorageRequest.getMemberId());
    }
}
