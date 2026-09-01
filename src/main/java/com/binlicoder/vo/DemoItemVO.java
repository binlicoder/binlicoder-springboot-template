package com.binlicoder.vo;

import com.binlicoder.entity.DemoItemEntity;

public record DemoItemVO(Long id, String name, boolean enabled) {

    public static DemoItemVO from(DemoItemEntity entity) {
        return new DemoItemVO(entity.getId(), entity.getName(), entity.isEnabled());
    }
}
