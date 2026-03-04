package com.wzc.lease.web.admin.controller.apartment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.FacilityInfo;
import com.wzc.lease.model.enums.ItemType;
import com.wzc.lease.web.admin.service.FacilityInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "配套管理")
@RestController
@RequestMapping("/admin/facility") // 定义当前控制器的统一请求路径前缀
public class FacilityController {

    @Autowired // 注入服务层接口
    private FacilityInfoService service;

    @Operation(summary = "[根据类型]查询配套信息列表")
    @GetMapping("list") // 处理 HTTP GET 请求，常用于查询数据
    public Result<List<FacilityInfo>> listFacility(@RequestParam(required = false) ItemType type) {
        // 准备一个空的“查询条件包装器queryWrapper”
        LambdaQueryWrapper<FacilityInfo> queryWrapper = new LambdaQueryWrapper<>();
        // 如果前端传了 type 参数（即 type 不为空），我们才去数据库里按 type 查找
        // 有了上面的空纸箱，可以根据if判断把eq等于这类的条件添加到纸箱里
        if (type != null) {
            // FacilityInfo::getType 是Java8的方法引用语法，:: 可以理解为"的"
            // 即 FacilityInfo类 的 getType方法，用来告诉MyBatis-Plus要操作的是哪个字段
            // 注意：getType() 有括号是执行这个方法拿到结果；::getType 没有括号是指向这个方法本身，告诉MyBatis-Plus"就是这个字段"
            // MyBatis-Plus拿到后会自动分析出对应的数据库列名，生成 WHERE type = #{type} 的SQL条件
            // 相比直接写字符串 "type"，方法引用的好处是写错了编译时就会报错，更加安全
            // 最后WHERE type = #{前端传来的值}
            queryWrapper.eq(FacilityInfo::getType, type);
        }
        List<FacilityInfo> list = service.list(queryWrapper);
        return Result.ok(list);
    }

    @Operation(summary = "新增或修改配套信息")
    @PostMapping("saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdate(@RequestBody FacilityInfo facilityInfo) {
        service.saveOrUpdate(facilityInfo);
        return Result.ok();
    }

    @Operation(summary = "根据id删除配套信息")
    @DeleteMapping("deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result removeFacilityById(@RequestParam Long id) {
        service.removeById(id);
        return Result.ok();
    }

}
