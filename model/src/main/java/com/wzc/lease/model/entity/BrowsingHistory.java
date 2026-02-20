package com.wzc.lease.model.entity; // 包声明，定义当前类所在命名空间

import com.baomidou.mybatisplus.annotation.TableField; // 导入 MyBatis-Plus 字段映射注解
import com.baomidou.mybatisplus.annotation.TableName; // 导入 MyBatis-Plus 表映射注解
import com.fasterxml.jackson.annotation.JsonFormat; // 导入 Jackson 注解，用于日期格式化
import io.swagger.v3.oas.annotations.media.Schema; // 导入 OpenAPI 文档注解
import lombok.AllArgsConstructor; // 导入 Lombok 注解，用于生成全参构造方法
import lombok.Data; // 导入 Lombok 注解，用于自动生成常用方法
import lombok.NoArgsConstructor; // 导入 Lombok 注解，用于生成无参构造方法

import java.util.Date; // 导入日期类型

/**
 * @TableName browsing_history
 */
@TableName(value = "browsing_history") // MyBatis-Plus 注解，指定实体对应的数据表
@Data // Lombok 注解，自动生成 getter/setter/toString 等方法
@AllArgsConstructor // Lombok 注解，自动生成全参数构造方法
@NoArgsConstructor // Lombok 注解，自动生成无参数构造方法
public class BrowsingHistory extends BaseEntity { // 定义实体类

    private static final long serialVersionUID = 1L; // 序列化版本号，用于反序列化兼容性控制

    @Schema(description = "用户id") // OpenAPI 注解，描述模型或字段含义
    @TableField("user_id") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Long userId; // 字段定义，保存实体属性

    @Schema(description = "房间id") // OpenAPI 注解，描述模型或字段含义
    @TableField("room_id") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Long roomId; // 字段定义，保存实体属性

    @Schema(description = "浏览时间") // OpenAPI 注解，描述模型或字段含义
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Jackson 注解，指定日期字段的序列化格式
    @TableField("browse_time") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Date browseTime; // 字段定义，保存实体属性

} // 代码块结束

