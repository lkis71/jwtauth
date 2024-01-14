package com.security.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    MASTER("MASTER"),
    ADMIN("ADMIN");

    private String value;
}
