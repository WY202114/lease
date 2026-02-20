package com.wzc.lease.model.entity; // 包声明，定义当前类所在命名空间

import com.baomidou.mybatisplus.annotation.IdType; // 导入 MyBatis-Plus 主键策略枚举
import com.baomidou.mybatisplus.annotation.TableField; // 导入 MyBatis-Plus 字段映射注解
import com.baomidou.mybatisplus.annotation.TableId; // 导入 MyBatis-Plus 主键映射注解
import io.swagger.v3.oas.annotations.media.Schema; // 导入 OpenAPI 文档注解
import lombok.Data; // 导入 Lombok 注解，用于自动生成常用方法

import java.io.Serializable; // 导入序列化接口
import java.util.Date; // 导入日期类型

@Data // Lombok 注解，自动生成 getter/setter/toString 等方法
public class BaseEntity implements Serializable { // 定义实体类

    @Schema(description = "主键") // OpenAPI 注解，描述模型或字段含义
    @TableId(value = "id", type = IdType.AUTO) // MyBatis-Plus 注解，指定主键字段及生成策略
    private Long id; // 字段定义，保存实体属性

    @Schema(description = "创建时间") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "create_time") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Date createTime; // 字段定义，保存实体属性

    @Schema(description = "更新时间") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "update_time") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Date updateTime; // 字段定义，保存实体属性

    @Schema(description = "逻辑删除") // OpenAPI 注解，描述模型或字段含义
    @TableField("is_deleted") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Byte isDeleted; // 字段定义，保存实体属性

} // 代码块结束

