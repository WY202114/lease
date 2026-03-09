package com.wzc.lease.model.entity; // 包声明，定义当前类所在命名空间

import com.wzc.lease.model.enums.BaseStatus; // 导入当前文件依赖的类型
import io.swagger.v3.oas.annotations.media.Schema; // 导入 OpenAPI 文档注解
import com.baomidou.mybatisplus.annotation.TableField; // 导入 MyBatis-Plus 字段映射注解
import com.baomidou.mybatisplus.annotation.TableName; // 导入 MyBatis-Plus 表映射注解

import lombok.AllArgsConstructor; // 导入 Lombok 注解，用于生成全参构造方法
import lombok.Builder; // 导入 Lombok 注解，用于构建者模式
import lombok.Data; // 导入 Lombok 注解，用于自动生成常用方法
import lombok.NoArgsConstructor; // 导入 Lombok 注解，用于生成无参构造方法

@Schema(description = "用户信息表") // OpenAPI 注解，描述模型或字段含义
@TableName(value = "user_info") // MyBatis-Plus 注解，指定实体对应的数据表
@Data // Lombok 注解，自动生成 getter/setter/toString 等方法
public class UserInfo extends BaseEntity { // 定义实体类

    private static final long serialVersionUID = 1L; // 序列化版本号，用于反序列化兼容性控制

    @Schema(description = "手机号码（用做登录用户名）") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "phone") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private String phone; // 字段定义，保存实体属性

    @Schema(description = "密码") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "password", select = false) // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    //select = false：mybatis-plus生成的通用mapper或service生成的sql就不会查询该字段
    private String password; // 字段定义，保存实体属性

    @Schema(description = "头像url") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "avatar_url") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private String avatarUrl; // 字段定义，保存实体属性

    @Schema(description = "昵称") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "nickname") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private String nickname; // 字段定义，保存实体属性

    @Schema(description = "账号状态") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "status") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private BaseStatus status; // 字段定义，保存实体属性


} // 代码块结束


