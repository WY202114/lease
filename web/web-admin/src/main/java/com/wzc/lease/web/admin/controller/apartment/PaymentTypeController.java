package com.wzc.lease.web.admin.controller.apartment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.PaymentType;
import com.wzc.lease.web.admin.service.PaymentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "支付方式管理")
@RequestMapping("/admin/payment")
@RestController // Spring 注解：标记这是一个控制器类，且所有方法的返回值会自动转为 JSON 返回给前端（相当于 @Controller +
                // @ResponseBody 的组合）
public class PaymentTypeController {

    @Autowired
    private PaymentTypeService service;

    /**
     * 逻辑说明：
     * 1. 支付方式表使用 is_deleted 作为逻辑删除标记；
     * 2. is_deleted = 0 表示未删除，is_deleted = 1 表示已删除；
     * 3. 本接口只查询 is_deleted = 0 的数据，因此返回结果中的 isDeleted 都是 0。
     */
    @Operation(summary = "查询全部支付方式列表")
    @GetMapping("list")
    /**
     * 查询支付方式列表（仅返回未删除的数据）。
     *
     * @return 支付方式列表
     */
    public Result<List<PaymentType>> listPaymentType() {

        // 构建查询条件
        QueryWrapper<PaymentType> paymentTypeQueryWrapper = new QueryWrapper<>();
        // 0 表示未删除
        int notDeleted = 0;
        // 只查询未删除的支付方式
        paymentTypeQueryWrapper.eq("is_deleted", notDeleted);
        // 执行查询并封装返回
        List<PaymentType> list = service.list(paymentTypeQueryWrapper);
        return Result.ok(list);
    }

    @Operation(summary = "保存或更新支付方式")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdatePaymentType(@RequestBody PaymentType paymentType) {
        // MyBatis-Plus 的 saveOrUpdate 方法：自动判断是新增还是更新
        // 判断依据：看实体对象的主键（id）是否有值
        // - 如果 id 为 null → 执行 save（INSERT），即新增一条记录
        // - 如果 id 不为 null → 执行 update（UPDATE），即根据 id 更新已有记录
        // 好处：前端不需要分别调用两个接口，一个方法同时搞定新增和修改
        service.saveOrUpdate(paymentType);
        return Result.ok();
    }

    @Operation(summary = "根据ID删除支付方式")
    @DeleteMapping("deleteById")
    public Result deletePaymentById(@RequestParam Long id) {
        // MyBatis-Plus 的 removeById 方法：根据主键（id）删除记录
        // 注意：因为我们在 BaseEntity 的 isDeleted 字段上加了 @TableLogic 注解
        // 所以这里**不会**执行 DELETE FROM 表名，而是执行 UPDATE payment_type SET is_deleted = 1 WHERE
        // id = ?
        // 也就是常说的"逻辑删除"，并非真的从数据库删掉
        service.removeById(id);
        return Result.ok();
    }

}
