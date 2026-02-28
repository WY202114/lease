package com.wzc.lease.web.admin.controller.system;


import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.SystemUser;
import com.wzc.lease.model.enums.BaseStatus;
import com.wzc.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.wzc.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


@Tag(name = "后台用户信息管理")
@RestController
@RequestMapping("/admin/system/user") // 定义当前控制器的统一请求路径前缀
public class SystemUserController {

    @Operation(summary = "根据条件分页查询后台用户列表")
    @GetMapping("page") // 处理 HTTP GET 请求，常用于查询数据
    public Result<IPage<SystemUserItemVo>> page(@RequestParam long current, @RequestParam long size, SystemUserQueryVo queryVo) {
        return Result.ok();
    }

    @Operation(summary = "根据ID查询后台用户信息")
    @GetMapping("getById") // 处理 HTTP GET 请求，按ID查询详情
    public Result<SystemUserItemVo> getById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "保存或更新后台用户信息")
    @PostMapping("saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdate(@RequestBody SystemUser systemUser) {
        return Result.ok();
    }

    @Operation(summary = "判断后台用户名是否可用")
    @GetMapping("isUserNameAvailable") // 处理 HTTP GET 请求，查询用户名可用性
    public Result<Boolean> isUsernameExists(@RequestParam String username) {
        return Result.ok();
    }

    @DeleteMapping("deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    @Operation(summary = "根据ID删除后台用户信息")
    public Result removeById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "根据ID修改后台用户状态")
    @PostMapping("updateStatusByUserId") // 处理 HTTP POST 请求，执行状态更新动作
    public Result updateStatusByUserId(@RequestParam Long id, @RequestParam BaseStatus status) {
        return Result.ok();
    }
}
