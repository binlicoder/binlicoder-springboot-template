package com.binlicoder.security;

import java.security.Principal;
import java.util.Set;

public record AuthenticatedUser(Long userId, String username, Set<String> roles) implements Principal {

    public AuthenticatedUser {
        roles = Set.copyOf(roles);
    }

    @Override
    public String getName() {
        return username;
    }
}
