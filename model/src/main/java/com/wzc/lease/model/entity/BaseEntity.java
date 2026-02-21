package com.wzc.lease.model.entity; // 包声明，定义当前类所在命名空间

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore; // 导入 Jackson 注解，用于在序列化为 JSON 时忽略某个字段
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
    @TableField(value = "create_time", fill = FieldFill.INSERT) // fill = FieldFill.INSERT 表示：在执行
                                                                // INSERT（新增记录）操作时，MyBatis-Plus 会自动将当前时间赋给该字段，省去手动 set
                                                                // 的麻烦
    @JsonIgnore // Jackson 注解：转为 JSON 时忽略该字段，即接口返回的数据中不会包含 createTime，前端看不到这个字段
    private Date createTime; // 字段定义，保存实体属性

    @Schema(description = "更新时间") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE) // fill = FieldFill.INSERT_UPDATE
                                                                       // 表示：在新增或修改记录时，MyBatis-Plus 都会自动将当前时间赋给该字段
    @JsonIgnore // 同上，接口返回的数据中不会包含 updateTime
    private Date updateTime; // 字段定义，保存实体属性

    @Schema(description = "逻辑删除") // OpenAPI 注解，描述模型或字段含义
    @TableField("is_deleted")
    @TableLogic // MyBatis-Plus 逻辑删除注解：删除时不真正删除数据，而是将 is_deleted 从 0 改为 1；查询时自动过滤已删除的记录
    // 注意：逻辑删除只对 MyBatis-Plus 自动生成的 SQL 生效，手写在 Mapper.xml 中的 SQL 不会自动过滤，需要自己加 WHERE
    // is_deleted = 0
    // 补充：所谓"自动生成的 SQL"是指调用 MyBatis-Plus 内置方法（如 list()、getById()、removeById()
    // 等）时，由框架自动拼接的 SQL
    // 而"手写 SQL"是指你自己在 Mapper.xml 的 <select>、<update> 等标签中编写的 SQL 语句
    @JsonIgnore // 同上，接口返回的数据中不会包含 isDeleted，逻辑删除标记不需要暴露给前端
    private Byte isDeleted; // 字段定义，保存实体属性

} // 代码块结束
