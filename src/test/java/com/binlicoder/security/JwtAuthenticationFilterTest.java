package com.binlicoder.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class JwtAuthenticationFilterTest {

    private final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(mock(JwtTokenService.class));

    @Test
    void shouldSkipExternalApiRequests() {
        var request = new MockHttpServletRequest("GET", "/api/external/orders");

        assertThat(filter.shouldNotFilter(request)).isTrue();
    }

    @Test
    void shouldFilterRegularApiRequests() {
        var request = new MockHttpServletRequest("GET", "/api/demo-items");

        assertThat(filter.shouldNotFilter(request)).isFalse();
    }
}
