package com.security.jwt.repository;

import com.security.member.entity.TokenStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JwtRepository {

    private final EntityManager em;

    public Optional<TokenStorage> findMemberToken(String refreshToken) {
        return Optional.ofNullable(em.createQuery(
                "select t " +
                        "from TokenStorage t " +
                        "join fetch t.member m " +
                        "where t.refreshToken = :refreshToken", TokenStorage.class)
                .setParameter("refreshToken", refreshToken)
                .getSingleResult());
    }

    public void save(TokenStorage tokenStorage) {
        em.persist(tokenStorage);
    }
}
