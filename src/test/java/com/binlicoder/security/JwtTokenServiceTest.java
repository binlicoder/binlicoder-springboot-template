package com.binlicoder.security;

import com.binlicoder.config.AppProperties;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenServiceTest {

    private final JwtTokenService service = new JwtTokenService(new AppProperties(
            new AppProperties.Security(
                    "binlicoder-springboot-template",
                    "a-secure-test-secret-with-more-than-32-characters",
                    Duration.ofHours(2),
                    "key"),
            new AppProperties.Cache(100, Duration.ofMinutes(5))));

    @Test
    void shouldRoundTripAccessToken() {
        String token = service.createAccessToken(42L, "tester", Set.of("USER"));

        AuthenticatedUser user = service.verify(token);

        assertThat(user.userId()).isEqualTo(42L);
        assertThat(user.username()).isEqualTo("tester");
        assertThat(user.roles()).containsExactly("USER");
    }

    @Test
    void shouldNormalizeRoles() {
        String token = service.createAccessToken(42L, "tester", Set.of(" admin "));

        assertThat(service.verify(token).roles()).containsExactly("ADMIN");
    }
}
