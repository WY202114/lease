package com.wzc.lease.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReleaseStatus implements BaseEnum {

    RELEASED(1, "已发布"),
    NOT_RELEASED(0, "未发布");


    // MyBatis-Plus 持久化枚举时，使用该字段值写入数据库
    @EnumValue
    // Jackson 序列化枚举时，使用该字段值输出到 JSON
    @JsonValue
    private Integer code;

    private String name;


    ReleaseStatus(Integer code, String name) {
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

