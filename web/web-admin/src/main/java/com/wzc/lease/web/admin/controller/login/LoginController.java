package com.wzc.lease.web.admin.controller.login;


import com.wzc.lease.common.result.Result;
import com.wzc.lease.web.admin.vo.login.CaptchaVo;
import com.wzc.lease.web.admin.vo.login.LoginVo;
import com.wzc.lease.web.admin.vo.system.user.SystemUserInfoVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理系统登录管理")
@RestController
@RequestMapping("/admin") // 定义当前控制器的统一请求路径前缀
public class LoginController {

    @Operation(summary = "获取图形验证码")
    @GetMapping("login/captcha") // 处理 HTTP GET 请求，常用于查询数据
    public Result<CaptchaVo> getCaptcha() {
        return Result.ok();
    }

    @Operation(summary = "登录")
    @PostMapping("login") // 处理 HTTP POST 请求，常用于提交登录表单
    public Result<String> login(@RequestBody LoginVo loginVo) {
        return Result.ok();
    }

    @Operation(summary = "获取登陆用户个人信息")
    @GetMapping("info") // 处理 HTTP GET 请求，查询当前登录用户信息
    public Result<SystemUserInfoVo> info() {
        return Result.ok();
    }
}
