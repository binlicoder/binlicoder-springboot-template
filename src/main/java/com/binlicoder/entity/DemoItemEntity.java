package com.binlicoder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.binlicoder.common.persistence.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("demo_item")
public class DemoItemEntity extends AuditableEntity {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private boolean enabled;

    @Version
    private long version;
}
