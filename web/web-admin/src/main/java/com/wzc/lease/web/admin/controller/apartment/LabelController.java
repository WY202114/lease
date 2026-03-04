package com.wzc.lease.web.admin.controller.apartment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.LabelInfo;
import com.wzc.lease.model.enums.ItemType;
import com.wzc.lease.web.admin.service.LabelInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "标签管理")
@RestController
@RequestMapping("/admin/label") // 定义当前控制器的统一请求路径前缀
public class LabelController {

    @Autowired
    private LabelInfoService service;

    @Operation(summary = "（根据类型）查询标签列表") // 根据类型查询，即需要过滤
    @GetMapping("list") // 处理 HTTP GET 请求，常用于查询数据
    // @RequestParam(required = false) 解释：
    // required = false 表示前端发送请求时，这个 type 参数是“非必填的”。
    // - 如果前端传了 type 参数（例如 /list?type=1），那么这里的 type 就有值，会参与后续的条件过滤。
    // - 如果前端没传这个参数（例如只请求了 /list），那么这里的 type 就是 null，程序也不会报错，后续在判断 type != null
    // 时就会跳过过滤条件，查出所有数据。
    public Result<List<LabelInfo>> labelList(@RequestParam(required = false) ItemType type) {
        // 1. 创建一个普通的 QueryWrapper 对象，用于构建查询条件，泛型指定为实体类 LabelInfo
        QueryWrapper<LabelInfo> queryWrapper = new QueryWrapper<>();

        // 2. 判断前端传来的 type 参数是否为空
        if (type != null) {
            // 3. 如果不为空，则添加相等查询条件：数据库字段 "type" 的值等于前端传来的 type 值
            queryWrapper.eq("type", type);
        }

        // 4. 调用业务层的 list 方法查询数据，并将构造好的条件对象 queryWrapper 传进去（原代码漏了此参数）
        List<LabelInfo> list = service.list(queryWrapper);

        // 5. 将查询出的列表封装进 Result 对象中，并返回统一成功响应给前端
        return Result.ok(list);
    }

    @Operation(summary = "新增或修改标签信息")
    @PostMapping("saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdateLabel(@RequestBody LabelInfo labelInfo) {
        // 保存或更新单条标签信息（根据主键是否存在自动判断新增或更新）
        service.saveOrUpdate(labelInfo);
        return Result.ok();
    }

    @Operation(summary = "根据id删除标签信息")
    @DeleteMapping("deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result deleteLabelById(@RequestParam Long id) {
        // 按标签主键 id 执行逻辑删除
        service.removeById(id);
        return Result.ok();
    }
}
