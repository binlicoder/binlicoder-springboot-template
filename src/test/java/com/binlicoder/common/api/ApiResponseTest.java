package com.binlicoder.common.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    @Test
    void shouldCreateSuccessfulResponse() {
        ApiResponse<String> response = ApiResponse.success("value");

        assertThat(response.code()).isEqualTo(200);
        assertThat(response.message()).isEqualTo("操作成功");
        assertThat(response.data()).isEqualTo("value");
        assertThat(response.timestamp()).isNotNull();
    }
}
