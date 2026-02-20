package com.wzc.lease.model.entity; // 包声明，定义当前类所在命名空间

import com.wzc.lease.model.enums.BaseStatus; // 导入当前文件依赖的类型
import com.baomidou.mybatisplus.annotation.TableField; // 导入 MyBatis-Plus 字段映射注解
import com.baomidou.mybatisplus.annotation.TableName; // 导入 MyBatis-Plus 表映射注解
import io.swagger.v3.oas.annotations.media.Schema; // 导入 OpenAPI 文档注解
import lombok.Data; // 导入 Lombok 注解，用于自动生成常用方法

/**
 * 岗位信息表
 * @TableName system_post
 */
@TableName(value = "system_post") // MyBatis-Plus 注解，指定实体对应的数据表
@Data // Lombok 注解，自动生成 getter/setter/toString 等方法
public class SystemPost extends BaseEntity { // 定义实体类

    private static final long serialVersionUID = 1L; // 序列化版本号，用于反序列化兼容性控制

    @Schema(description = "岗位编码") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "code") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private String postCode; // 字段定义，保存实体属性

    @Schema(description = "岗位名称") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "name") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private String name; // 字段定义，保存实体属性

    @Schema(description = "岗位描述信息") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "description") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private String description; // 字段定义，保存实体属性

    @Schema(description = "岗位状态") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "status") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private BaseStatus status; // 字段定义，保存实体属性


} // 代码块结束


