package com.wzc.lease.web.admin.controller.user;


import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.UserInfo;
import com.wzc.lease.model.enums.BaseStatus;
import com.wzc.lease.web.admin.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户信息管理")
@RestController
@RequestMapping("/admin/user") // 定义当前控制器的统一请求路径前缀
public class UserInfoController {

    @Operation(summary = "分页查询用户信息")
    @GetMapping("page") // 处理 HTTP GET 请求，常用于查询数据
    public Result<IPage<UserInfo>> pageUserInfo(@RequestParam long current, @RequestParam long size, UserInfoQueryVo queryVo) {
        return Result.ok();
    }

    @Operation(summary = "根据用户id更新账号状态")
    @PostMapping("updateStatusById") // 处理 HTTP POST 请求，执行状态更新动作
    public Result updateStatusById(@RequestParam Long id, @RequestParam BaseStatus status) {
        return Result.ok();
    }
}
