package com.wzc.lease.web.admin.controller.apartment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.FeeKey;
import com.wzc.lease.model.entity.FeeValue;
import com.wzc.lease.web.admin.service.FeeKeyService;
import com.wzc.lease.web.admin.service.FeeValueService;
import com.wzc.lease.web.admin.vo.fee.FeeKeyVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * FeeController 整体调用流程说明
 *
 * 本控制器负责管理杂费相关的增删改查。杂费分为两张表：
 * - fee_key 表：存储杂费名称（如水费、电费、物业费）
 * - fee_value 表：存储杂费值（如元/度、元/月），每个杂费名下可以有多个杂费值（一对多关系）
 *
 * 以 feeInfoList() 查询接口为例，整个调用链路如下：
 *
 * 前端 GET /admin/fee/list
 *        |
 * FeeController.feeInfoList()          -- 第1层：Controller 接收请求，自己不干活，转发给 Service
 *        |
 * FeeKeyServiceImpl.feeInfoList()      -- 第2层：Service 业务层，调用 Mapper 获取数据
 *        |
 * FeeKeyMapper.feeInfoList()           -- 第3层：Mapper 接口，只声明方法名，没有方法体
 *        |
 * FeeKeyMapper.xml select id=feeInfoList  -- 第4层：XML 里写真正的 SQL，MyBatis 通过方法名匹配到这里执行
 *        |
 * 数据库返回多行扁平数据（因为 left join 产生一对多的多行记录）
 *        |
 * resultMap + collection               -- MyBatis 根据 resultMap 把多行扁平数据合并折叠成嵌套的 FeeKeyVo 对象
 *        |
 * 一路返回给前端：List of FeeKeyVo（每个 FeeKeyVo 里面包含一个 List of FeeValue）
 *
 * 每一层各司其职：Controller 接请求 - Service 处理业务 - Mapper 访问数据库 - XML 写 SQL
 */
@Tag(name = "房间杂费管理")
@RestController
@RequestMapping("/admin/fee") // 定义当前控制器的统一请求路径前缀
public class FeeController {

    @Autowired
    private FeeKeyService feeKeyService;

    @Autowired
    private FeeValueService feeValueService;

    @Operation(summary = "保存或更新杂费名称")
    @PostMapping("key/saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdateFeeKey(@RequestBody FeeKey feeKey) {
        feeKeyService.saveOrUpdate(feeKey);
        return Result.ok();
    }

    /*
     * 操作类（增删改）：前端只看成没成功，用 Result.ok()。
     * 查询类（查）：前端不仅要看成没成功，还要拿拿数据去用，用 Result.ok(你要给的数据)。
     */

    @Operation(summary = "保存或更新杂费值")
    @PostMapping("value/saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdateFeeValue(@RequestBody FeeValue feeValue) {
        feeValueService.saveOrUpdate(feeValue);
        return Result.ok();
    }

    @Operation(summary = "查询全部杂费名称和杂费值列表")
    @GetMapping("list") // 处理 HTTP GET 请求，常用于查询数据
    public Result<List<FeeKeyVo>> feeInfoList() {
        List<FeeKeyVo> list = feeKeyService.feeInfoList();
        return Result.ok(list);
    }

    @Operation(summary = "根据id删除杂费名称")
    @DeleteMapping("key/deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result deleteFeeKeyById(@RequestParam Long feeKeyId) {
        feeKeyService.removeById(feeKeyId);// 删除杂费名称

        // 删除杂费名称下的属性值
        LambdaQueryWrapper<FeeValue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FeeValue::getFeeKeyId, feeKeyId);
        return Result.ok();
    }

    @Operation(summary = "根据id删除杂费值")
    @DeleteMapping("value/deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result deleteFeeValueById(@RequestParam Long id) {
        feeValueService.removeById(id);
        return Result.ok();
    }
}
