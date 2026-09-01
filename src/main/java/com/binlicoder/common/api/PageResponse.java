package com.binlicoder.common.api;

import java.util.List;

public record PageResponse<T>(List<T> records, long total, long current, long size) {

    public PageResponse {
        records = List.copyOf(records);
    }
}
