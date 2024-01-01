package com.security.member.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
public class Member implements UserDetails {

    @Id
    @Column(name = "member_id")
    private String id;

    private String password;

    private String role;

    @Builder
    public Member(String id, String password, String role) {
        this.id = id;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(role.split(",")).forEach(v -> authorities.add(new SimpleGrantedAuthority(v)));

        return authorities;
    }

    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
