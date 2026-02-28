package com.wzc.lease.web.admin.controller.system;

import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.SystemPost;
import com.wzc.lease.model.enums.BaseStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Tag(name = "后台用户岗位管理")
@RequestMapping("/admin/system/post") // 定义当前控制器的统一请求路径前缀
public class SystemPostController {

    @Operation(summary = "分页获取岗位信息")
    @GetMapping("page") // 处理 HTTP GET 请求，常用于查询数据
    private Result<IPage<SystemPost>> page(@RequestParam long current, @RequestParam long size) {
        return Result.ok();
    }

    @Operation(summary = "保存或更新岗位信息")
    @PostMapping("saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdate(@RequestBody SystemPost systemPost) {
        return Result.ok();
    }

    @DeleteMapping("deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    @Operation(summary = "根据id删除岗位")
    public Result removeById(@RequestParam Long id) {

        return Result.ok();
    }

    @GetMapping("getById") // 处理 HTTP GET 请求，按ID查询详情
    @Operation(summary = "根据id获取岗位信息")
    public Result<SystemPost> getById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "获取全部岗位列表")
    @GetMapping("list") // 处理 HTTP GET 请求，常用于查询列表
    public Result<List<SystemPost>> list() {
        return Result.ok();
    }

    @Operation(summary = "根据岗位id修改状态")
    @PostMapping("updateStatusByPostId") // 处理 HTTP POST 请求，执行状态更新动作
    public Result updateStatusByPostId(@RequestParam Long id, @RequestParam BaseStatus status) {
        return Result.ok();
    }
}
