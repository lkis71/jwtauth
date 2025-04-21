package com.security.repository;

import com.security.redis.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JwtRepository {

    private final EntityManager em;

    public Optional<RefreshToken> findMemberToken(String refreshToken) {
        return Optional.ofNullable(em.createQuery(
                "select t " +
                        "from RefreshToken t " +
                        "join fetch t.member m " +
                        "where t.refreshToken = :refreshToken", RefreshToken.class)
                .setParameter("refreshToken", refreshToken)
                .getSingleResult());
    }

    public void save(RefreshToken refreshToken) {
        em.persist(refreshToken);
    }
}
