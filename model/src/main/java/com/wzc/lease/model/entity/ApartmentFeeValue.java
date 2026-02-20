package com.wzc.lease.model.entity; // 包声明，定义当前类所在命名空间

import io.swagger.v3.oas.annotations.media.Schema; // 导入 OpenAPI 文档注解
import com.baomidou.mybatisplus.annotation.TableField; // 导入 MyBatis-Plus 字段映射注解
import com.baomidou.mybatisplus.annotation.TableName; // 导入 MyBatis-Plus 表映射注解
import io.swagger.v3.oas.annotations.media.Schema; // 导入 OpenAPI 文档注解
import lombok.Builder; // 导入 Lombok 注解，用于构建者模式
import lombok.Data; // 导入 Lombok 注解，用于自动生成常用方法

@Schema(description = "公寓&杂费关联表") // OpenAPI 注解，描述模型或字段含义
@TableName(value = "apartment_fee_value") // MyBatis-Plus 注解，指定实体对应的数据表
@Data // Lombok 注解，自动生成 getter/setter/toString 等方法
@Builder // Lombok 注解，支持使用构建者方式创建对象
public class ApartmentFeeValue extends BaseEntity { // 定义实体类

    private static final long serialVersionUID = 1L; // 序列化版本号，用于反序列化兼容性控制

    @Schema(description = "公寓id") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "apartment_id") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Long apartmentId; // 字段定义，保存实体属性

    @Schema(description = "收费项value_id") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "fee_value_id") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Long feeValueId; // 字段定义，保存实体属性

} // 代码块结束


