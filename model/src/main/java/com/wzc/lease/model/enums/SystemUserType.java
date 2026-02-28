package com.wzc.lease.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SystemUserType implements BaseEnum {

    ADMIN(0, "管理员"),
    COMMON(1, "普通用户");

    // MyBatis-Plus 持久化枚举时，使用该字段值写入数据库
    @EnumValue
    // Jackson 序列化枚举时，使用该字段值输出到 JSON
    @JsonValue
    private Integer code;

    private String name;


    SystemUserType(Integer code, String name) {
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

