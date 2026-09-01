package com.binlicoder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DemoItemSaveDTO(
        @NotBlank(message = "名称不能为空")
        @Size(max = 100, message = "名称长度不能超过100个字符")
        String name,
        boolean enabled
) {

    public DemoItemSaveDTO {
        if (name != null) {
            name = name.trim();
        }
    }
}
