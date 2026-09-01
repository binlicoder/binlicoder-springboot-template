package com.binlicoder.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties("app")
public record AppProperties(
        @Valid @NotNull Security security,
        @Valid @NotNull Cache cache
) {

    public record Security(
            @NotBlank String issuer,
            @NotBlank @Size(min = 32) String jwtSecret,
            @NotNull Duration accessTokenTtl,
            @NotBlank String apiKey
    ) {
    }

    public record Cache(@Min(1) long maximumSize, @NotNull Duration ttl) {
    }
}
