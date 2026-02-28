package com.wzc.lease.web.admin.controller.apartment;


import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.AttrKey;
import com.wzc.lease.model.entity.AttrValue;
import com.wzc.lease.web.admin.vo.attr.AttrKeyVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "房间属性管理")
@RestController
@RequestMapping("/admin/attr") // 定义当前控制器的统一请求路径前缀
public class AttrController {

    @Operation(summary = "新增或更新属性名称")
    @PostMapping("key/saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdateAttrKey(@RequestBody AttrKey attrKey) {
        return Result.ok();
    }

    @Operation(summary = "新增或更新属性值")
    @PostMapping("value/saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdateAttrValue(@RequestBody AttrValue attrValue) {
        return Result.ok();
    }


    @Operation(summary = "查询全部属性名称和属性值列表")
    @GetMapping("list") // 处理 HTTP GET 请求，常用于查询数据
    public Result<List<AttrKeyVo>> listAttrInfo() {
        return Result.ok();
    }

    @Operation(summary = "根据id删除属性名称")
    @DeleteMapping("key/deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result removeAttrKeyById(@RequestParam Long attrKeyId) {
        return Result.ok();
    }

    @Operation(summary = "根据id删除属性值")
    @DeleteMapping("value/deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result removeAttrValueById(@RequestParam Long id) {
        return Result.ok();
    }

}
