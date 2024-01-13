package com.security.member.entity;

import com.security.jwt.dto.TokenStorageRequest;
import lombok.*;

import javax.persistence.*;

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
    private Member member;

    private String ipAddress;

    @Builder
    public TokenStorage(Long id, String refreshToken, Member member, String ipAddress) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.member = member;
        this.ipAddress = ipAddress;
    }

    public Boolean isValidToken(TokenStorageRequest tokenStorageRequest) {
        return this.refreshToken.equals(tokenStorageRequest.getRefreshToken());
//            && this.ipAddress.equals(tokenStorageRequest.getIpAddress());
    }

    public void updateRefreshToken(String refreshToken) {
        this.setRefreshToken(refreshToken);
    }
}
