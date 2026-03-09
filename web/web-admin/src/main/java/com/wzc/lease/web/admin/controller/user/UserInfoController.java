package com.wzc.lease.web.admin.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.UserInfo;
import com.wzc.lease.model.enums.BaseStatus;
import com.wzc.lease.web.admin.service.UserInfoService;
import com.wzc.lease.web.admin.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息管理 Controller
 * <p>
 * 为什么这里没有像 LeaseAgreementController 那样自定义 Mapper XML 中的 SQL？
 * 因为用户信息查询只涉及 user_info 一张表，不需要 JOIN 其他表，
 * MyBatis-Plus 的通用 service.page() 方法就能自动生成 SELECT 语句，
 * 配合 LambdaQueryWrapper 在 Java 代码里动态拼条件，比写 XML 更简洁。
 * 只有多表联查时（如 LeaseAgreement 需要 JOIN 公寓、房间、支付方式等表），
 * 才需要手写 XML SQL。
 * </p>
 *
 * @author 武振川
 * @version 1.0
 */
@Tag(name = "用户信息管理")
@RestController
@RequestMapping("/admin/user")
public class UserInfoController {

    @Autowired
    private UserInfoService service;

    /**
     * 分页查询用户信息
     * <p>
     * 为什么用 LambdaQueryWrapper 而不是在 XML 里写 SQL？
     * 因为这里只查 user_info 单表，条件也很简单（手机号模糊搜索 + 状态精确匹配），
     * 用 LambdaQueryWrapper 在 Java 代码里拼条件比写 XML 更直观，改起来也方便。
     * 而且 Lambda 写法（UserInfo::getPhone）有编译期检查，字段名改了会直接报错，
     * 比 XML 里写字符串 "phone" 更不容易出 bug。
     * </p>
     */
    @Operation(summary = "分页查询用户信息")
    @GetMapping("page")
    public Result<IPage<UserInfo>> pageUserInfo(@RequestParam long current, @RequestParam long size,
            UserInfoQueryVo queryVo, ServletRequest servletRequest) {

        Page<UserInfo> page = new Page<>(current, size);

        // 为什么不直接写 SQL 字符串，而要用 LambdaQueryWrapper？
        // 因为用 Lambda 表达式引用字段（如 UserInfo::getPhone），
        // 如果以后实体类字段名改了，编译器会直接报错，避免运行时才发现 SQL 写错了。
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();

        // 为什么 like() 的第一个参数要写 queryVo.getPhone() != null？
        // 这是 MyBatis-Plus 的"条件构造"语法——第一个参数是 boolean 开关：
        // - 如果前端没传 phone（为 null），开关为 false，这个 like 条件不会拼进 SQL
        // - 如果前端传了 phone，开关为 true，才会拼出 WHERE phone LIKE '%xxx%'
        // 这样就避免了手动写 if (phone != null) { ... } 的判断逻辑，一行搞定动态条件。
        // 为什么用 like 而不是 eq？因为手机号查询通常支持模糊搜索（输入部分号码也能搜到）。
        queryWrapper.like(queryVo.getPhone() != null, UserInfo::getPhone, queryVo.getPhone());

        // 为什么 status 用 eq（精确匹配）而不是 like（模糊匹配）？
        // 因为状态是枚举值（如"正常"或"禁用"），只有固定的几个选项，
        // 不存在"输入一部分状态"的场景，所以必须精确匹配。
        queryWrapper.eq(queryVo.getStatus() != null, UserInfo::getStatus, queryVo.getStatus());

        // 为什么直接调 service.page() 而不像 LeaseAgreement 那样调自定义的 pageAgreement()？
        // 因为 service.page() 是 MyBatis-Plus 内置的通用分页方法，
        // 它会自动根据 page 里的 current/size 拼接 LIMIT，根据 queryWrapper 拼接 WHERE，
        // 单表查询完全够用，没必要再去 XML 里写一遍 SQL。
        Page<UserInfo> result = service.page(page, queryWrapper);

        return Result.ok(result);
    }

    /**
     * 根据用户 ID 更新账号状态（如 启用/禁用）
     * <p>
     * 为什么用 LambdaUpdateWrapper 而不是直接传一个 UserInfo 对象给 updateById()？
     * 因为这里只需要改 status 一个字段。如果用 updateById()，
     * 需要先查出整个 UserInfo 对象，再修改 status，再保存回去——多了一次查询。
     * 用 LambdaUpdateWrapper 可以直接告诉数据库"只改这一个字段"，
     * 一条 UPDATE SQL 搞定，不需要先 SELECT。
     * </p>
     */
    @Operation(summary = "根据用户id更新账号状态")
    @PostMapping("updateStatusById")
    public Result updateStatusById(@RequestParam Long id, @RequestParam BaseStatus status) {

        LambdaUpdateWrapper<UserInfo> userUpdateWrapper = new LambdaUpdateWrapper<>();

        // eq 定位到要更新的那一行：WHERE id = ?
        userUpdateWrapper.eq(UserInfo::getId, id);

        // set 指定只更新 status 字段：SET status = ?
        // 为什么用 set() 而不是直接构造一个 UserInfo 对象？
        // 因为 set() 可以精确控制只更新哪些字段，
        // 如果构造 UserInfo 对象，其他字段为 null 可能会把数据库的值覆盖成 null。
        userUpdateWrapper.set(UserInfo::getStatus, status);

        // 最终只执行一条 SQL：UPDATE user_info SET status = ? WHERE id = ?
        // 没有多余的 SELECT 查询，效率更高。
        service.update(userUpdateWrapper);

        return Result.ok();
    }
}
