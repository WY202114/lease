package com.wzc.lease.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;


public enum LeaseSourceType implements BaseEnum {

    NEW(1, "新签"),
    RENEW(2, "续约");

    // Jackson 序列化枚举时，使用该字段值输出到 JSON
    @JsonValue
    // MyBatis-Plus 持久化枚举时，使用该字段值写入数据库
    @EnumValue
    private Integer code;

    private String name;

    LeaseSourceType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }
}

