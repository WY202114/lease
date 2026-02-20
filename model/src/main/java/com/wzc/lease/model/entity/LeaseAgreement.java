package com.wzc.lease.model.entity; // 包声明，定义当前类所在命名空间

import io.swagger.v3.oas.annotations.media.Schema; // 导入 OpenAPI 文档注解
import com.atguigu.lease.model.enums.LeaseSourceType; // 导入当前文件依赖的类型
import com.atguigu.lease.model.enums.LeaseStatus; // 导入当前文件依赖的类型
import com.baomidou.mybatisplus.annotation.TableField; // 导入 MyBatis-Plus 字段映射注解
import com.baomidou.mybatisplus.annotation.TableName; // 导入 MyBatis-Plus 表映射注解
import com.fasterxml.jackson.annotation.JsonFormat; // 导入 Jackson 注解，用于日期格式化
import lombok.Data; // 导入 Lombok 注解，用于自动生成常用方法

import java.math.BigDecimal; // 导入高精度小数类型
import java.util.Date; // 导入日期类型

@Schema(description = "租约信息表") // OpenAPI 注解，描述模型或字段含义
@TableName(value = "lease_agreement") // MyBatis-Plus 注解，指定实体对应的数据表
@Data // Lombok 注解，自动生成 getter/setter/toString 等方法
public class LeaseAgreement extends BaseEntity { // 定义实体类

    private static final long serialVersionUID = 1L; // 序列化版本号，用于反序列化兼容性控制

    @Schema(description = "承租人手机号码") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "phone") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private String phone; // 字段定义，保存实体属性

    @Schema(description = "承租人姓名") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "name") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private String name; // 字段定义，保存实体属性

    @Schema(description = "承租人身份证号码") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "identification_number") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private String identificationNumber; // 字段定义，保存实体属性

    @Schema(description = "签约公寓id") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "apartment_id") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Long apartmentId; // 字段定义，保存实体属性

    @Schema(description = "签约房间id") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "room_id") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Long roomId; // 字段定义，保存实体属性

    @Schema(description = "租约开始日期") // OpenAPI 注解，描述模型或字段含义
    @JsonFormat(pattern = "yyyy-MM-dd") // Jackson 注解，指定日期字段的序列化格式
    @TableField(value = "lease_start_date") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Date leaseStartDate; // 字段定义，保存实体属性

    @Schema(description = "租约结束日期") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "lease_end_date") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    @JsonFormat(pattern = "yyyy-MM-dd") // Jackson 注解，指定日期字段的序列化格式
    private Date leaseEndDate; // 字段定义，保存实体属性

    @Schema(description = "租期id") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "lease_term_id") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Long leaseTermId; // 字段定义，保存实体属性

    @Schema(description = "租金（元/月）") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "rent") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private BigDecimal rent; // 字段定义，保存实体属性

    @Schema(description = "押金（元）") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "deposit") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private BigDecimal deposit; // 字段定义，保存实体属性

    @Schema(description = "支付类型id") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "payment_type_id") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private Long paymentTypeId; // 字段定义，保存实体属性

    @Schema(description = "租约状态") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "status") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private LeaseStatus status; // 字段定义，保存实体属性

    @Schema(description = "租约来源") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "source_type") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private LeaseSourceType sourceType; // 字段定义，保存实体属性

    @Schema(description = "备注信息") // OpenAPI 注解，描述模型或字段含义
    @TableField(value = "additional_info") // MyBatis-Plus 注解，指定实体字段对应的数据库字段
    private String additionalInfo; // 字段定义，保存实体属性

} // 代码块结束


