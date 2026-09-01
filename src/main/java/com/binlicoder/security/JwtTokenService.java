package com.binlicoder.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.binlicoder.config.AppProperties;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class JwtTokenService {

    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String ACCESS_TOKEN = "access";
    private final AppProperties properties;

    public JwtTokenService(AppProperties properties) {
        this.properties = properties;
    }

    public String createAccessToken(Long userId, String username, Set<String> roles) {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(username, "username must not be null");
        Set<String> normalizedRoles = normalizeRoles(roles);
        Instant now = Instant.now();
        return JWT.create()
                .withIssuer(properties.security().issuer())
                .withSubject(userId.toString())
                .withClaim("username", username)
                .withClaim("roles", List.copyOf(normalizedRoles))
                .withClaim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN)
                .withIssuedAt(now)
                .withExpiresAt(now.plus(properties.security().accessTokenTtl()))
                .sign(algorithm());
    }

    public AuthenticatedUser verify(String token) {
        DecodedJWT jwt = JWT.require(algorithm())
                .withIssuer(properties.security().issuer())
                .withClaim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN)
                .build()
                .verify(token);
        List<String> roles = jwt.getClaim("roles").asList(String.class);
        return new AuthenticatedUser(
                Long.valueOf(jwt.getSubject()),
                jwt.getClaim("username").asString(),
                normalizeRoles(roles));
    }

    private Algorithm algorithm() {
        return Algorithm.HMAC256(properties.security().jwtSecret());
    }

    private static Set<String> normalizeRoles(Collection<String> roles) {
        if (roles == null) {
            return Set.of();
        }
        return roles.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .map(String::toUpperCase)
                .collect(java.util.stream.Collectors.toUnmodifiableSet());
    }
}
