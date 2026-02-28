package com.wzc.lease.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;


public enum BaseStatus implements BaseEnum {


    ENABLE(1, "正常"),

    DISABLE(0, "禁用");


    // MyBatis-Plus 持久化枚举时，使用该字段值写入数据库
    @EnumValue
    // Jackson 序列化枚举时，使用该字段值输出到 JSON
    @JsonValue
    private Integer code;

    private String name;

    BaseStatus(Integer code, String name) {
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

