package com.wzc.lease.web.admin.controller.apartment;


import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.FeeKey;
import com.wzc.lease.model.entity.FeeValue;
import com.wzc.lease.web.admin.vo.fee.FeeKeyVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "房间杂费管理")
@RestController
@RequestMapping("/admin/fee") // 定义当前控制器的统一请求路径前缀
public class FeeController {

    @Operation(summary = "保存或更新杂费名称")
    @PostMapping("key/saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdateFeeKey(@RequestBody FeeKey feeKey) {
        return Result.ok();
    }

    @Operation(summary = "保存或更新杂费值")
    @PostMapping("value/saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdateFeeValue(@RequestBody FeeValue feeValue) {
        return Result.ok();
    }


    @Operation(summary = "查询全部杂费名称和杂费值列表")
    @GetMapping("list") // 处理 HTTP GET 请求，常用于查询数据
    public Result<List<FeeKeyVo>> feeInfoList() {
        return Result.ok();
    }

    @Operation(summary = "根据id删除杂费名称")
    @DeleteMapping("key/deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result deleteFeeKeyById(@RequestParam Long feeKeyId) {
        return Result.ok();
    }

    @Operation(summary = "根据id删除杂费值")
    @DeleteMapping("value/deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result deleteFeeValueById(@RequestParam Long id) {
        return Result.ok();
    }
}
