package com.wzc.lease.web.admin.controller.login;

import com.wzc.lease.common.login.LoginUserHolder;
import com.wzc.lease.common.result.Result;
import com.wzc.lease.common.utils.JwtUtil;
import com.wzc.lease.web.admin.service.LoginService;
import com.wzc.lease.web.admin.vo.login.CaptchaVo;
import com.wzc.lease.web.admin.vo.login.LoginVo;
import com.wzc.lease.web.admin.vo.system.user.SystemUserInfoVo;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台管理系统登录管理")
@RestController
@RequestMapping("/admin") // 定义当前控制器的统一请求路径前缀
public class LoginController {

    @Autowired
    private LoginService service;

    @Operation(summary = "获取图形验证码")
    @GetMapping("login/captcha") // 处理 HTTP GET 请求，常用于查询数据
    public Result<CaptchaVo> getCaptcha() {
        CaptchaVo result = service.getCaptcha();
        return Result.ok(result);
    }

    @Operation(summary = "登录")
    @PostMapping("login") // 处理 HTTP POST 请求，常用于提交登录表单
    public Result<String> login(@RequestBody LoginVo loginVo) {
        String jwt = service.login(loginVo);
        return Result.ok(jwt);
    }

    /**
     * 获取当前登录用户的个人信息
     *
     * 【重构后的最新逻辑（基于 ThreadLocal）】：
     * 1. 保安（AuthenticationInterceptor）在门口拦下请求验票时，不仅验证了真伪，
     * 还顺手把票里的信息（userId, username）抠了出来，放进了一个名为 LoginUserHolder 的“储物柜”里。
     * 2. 这个“储物柜”底层使用了 ThreadLocal 技术。它的神奇之处在于：每个 HTTP 请求进来都会分配一个独立的线程，
     * ThreadLocal 能保证“存进去的东西，只有当前这个线程能拿到”，天然做到了不同用户请求间的数据隔离。
     * 3. 既然保安在最前面已经验过票并且存进储物柜了，我们这里的 Controller 就不用再自己去拿 Token 重新解析一遍了！
     * 4. 这里的业务代码只需直接拉开储物柜（LoginUserHolder.getLoginUser()），拿到专属的 userId 即可。
     */
    @Operation(summary = "获取登陆用户个人信息")
    @GetMapping("info")
    // public Result<SystemUserInfoVo> info(@RequestHeader("access-token") String
    // token) {
    // 【问题1：为什么不需要声明 @RequestHeader 参数了？】
    // 答：因为现在业务逻辑根本不需要原原本本的 Token 字符串了。Token 解析的脏活累活全被前置的拦截器干完了。
    // 这使得 Controller 非常干净，彻底解耦了 HTTP 头部的参数传递。
    public Result<SystemUserInfoVo> info() {

        // 【问题2：为什么替换掉下面解析门票的代码？】
        // 答：如果每个接口都写一遍 JwtUtil.parseToken(token)，由于系统将来可能会有几百个接口需要用到用户身份，
        // 代码会极度冗余，且多次毫无意义的字符串解析会损耗性能。
        // Claims claims = JwtUtil.parseToken(token);
        // Long userId = claims.get("userId", Long.class);

        // 现在，直接从当前线程专属的“储物柜”（ThreadLocal）中提取出已经准备好的用户信息，优雅高效！
        Long userId = LoginUserHolder.getLoginUser().getUserId();

        // 带着 userId 去系统档案库查这个人的详细资料（姓名、头像等）
        SystemUserInfoVo systemUserInfoVo = service.getLoginUserInfoById(userId);

        // 封装返回
        return Result.ok(systemUserInfoVo);
    }
}
