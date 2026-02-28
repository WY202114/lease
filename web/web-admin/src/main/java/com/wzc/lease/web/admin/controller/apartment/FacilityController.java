package com.wzc.lease.web.admin.controller.apartment;


import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.FacilityInfo;
import com.wzc.lease.model.enums.ItemType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "配套管理")
@RestController
@RequestMapping("/admin/facility") // 定义当前控制器的统一请求路径前缀
public class FacilityController {

    @Operation(summary = "[根据类型]查询配套信息列表")
    @GetMapping("list") // 处理 HTTP GET 请求，常用于查询数据
    public Result<List<FacilityInfo>> listFacility(@RequestParam(required = false) ItemType type) {
        return Result.ok();
    }

    @Operation(summary = "新增或修改配套信息")
    @PostMapping("saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdate(@RequestBody FacilityInfo facilityInfo) {
        return Result.ok();
    }

    @Operation(summary = "根据id删除配套信息")
    @DeleteMapping("deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result removeFacilityById(@RequestParam Long id) {
        return Result.ok();
    }

}
