package com.wzc.lease.web.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.SystemUser;
import com.wzc.lease.model.enums.BaseStatus;
import com.wzc.lease.web.admin.service.SystemUserService;
import com.wzc.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.wzc.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台用户信息管理")
@RestController
@RequestMapping("/admin/system/user") // 定义当前控制器的统一请求路径前缀
public class SystemUserController {

    @Autowired
    private SystemUserService service;

    @Operation(summary = "根据条件分页查询后台用户列表")
    @GetMapping("page") // 处理 HTTP GET 请求，常用于查询数据
    public Result<IPage<SystemUserItemVo>> page(@RequestParam long current, @RequestParam long size,
            SystemUserQueryVo queryVo) {
        // 涉及多表查询
        // 答：Page 对象是 MyBatis-Plus 分页查询的“分页配置”，它告诉数据库：从第 current 页开始，每页取 size 条记录。
        // 它本身不包含数据，只是一个“取数据的指令”。真正的数据会在 service 层查询后填充到 Page 中返回。
        Page<SystemUser> page = new Page<>(current, size);
        // 答：page 参数告诉 MyBatis-Plus “要第几页、每页多少条”，框架会自动拼接 LIMIT 子句；
        // queryVo 是前端传来的查询条件（如姓名、手机号），用于在 Mapper XML 中拼接 WHERE 条件进行筛选。
        // 两者缺一不可：page 控制分页，queryVo 控制过滤。
        IPage<SystemUserItemVo> result = service.pageSystemUser(page, queryVo);
        return Result.ok(result);
    }

    @Operation(summary = "根据ID查询后台用户信息")
    @GetMapping("getById") // 处理 HTTP GET 请求，按ID查询详情
    public Result<SystemUserItemVo> getById(@RequestParam Long id) {
        SystemUserItemVo result = service.getSystemUserById(id);
        return Result.ok(result);
    }

    @Operation(summary = "保存或更新后台用户信息")
    @PostMapping("saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdate(@RequestBody SystemUser systemUser) {
        if (systemUser.getPassword() != null) {
            systemUser.setPassword(DigestUtils.md5Hex(systemUser.getPassword()));// md5Hex不允许为空（在修改员工信息时，前端可能传来的为空）
        }
        service.saveOrUpdate(systemUser);
        return Result.ok();
    }

    @Operation(summary = "判断后台用户名是否可用")
    @GetMapping("isUserNameAvailable") // 处理 HTTP GET 请求，查询用户名可用性
    public Result<Boolean> isUsernameExists(@RequestParam String username) {
        // 判断用户名是否可用（即数据库中是否已存在同名用户）
        // 1. 创建 LambdaQueryWrapper 条件构造器，用于构建 SELECT COUNT(*) 的 WHERE 条件
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        // 2. 设置查询条件：WHERE name = #{username}，精确匹配传入的用户名
        queryWrapper.eq(SystemUser::getName, username);
        // 3. 调用 service.count() 执行查询，返回符合条件的记录数
        // 最终 SQL 类似：SELECT COUNT(*) FROM system_user WHERE name = ?
        long count = service.count(queryWrapper);
        // 4. 如果 count == 0，说明没有重名用户，用户名可用，返回 true
        // 如果 count > 0，说明已有同名用户，用户名不可用，返回 false
        return Result.ok(count == 0);
    }

    @DeleteMapping("deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    @Operation(summary = "根据ID删除后台用户信息")
    public Result removeById(@RequestParam Long id) {
        service.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "根据ID修改后台用户状态")
    @PostMapping("updateStatusByUserId") // 处理 HTTP POST 请求，执行状态更新动作
    public Result updateStatusByUserId(@RequestParam Long id, @RequestParam BaseStatus status) {
        // 创建 LambdaUpdateWrapper 条件构造器，用于构建 UPDATE 语句
        LambdaUpdateWrapper<SystemUser> updateWrapper = new LambdaUpdateWrapper<>();
        // 设置 WHERE 条件：WHERE id = ?（定位到要修改的那条用户记录）
        // 注意：.eq() 并不是"查找用户"，而是生成 SQL 的 WHERE 子句，告诉数据库"更新哪一行"
        updateWrapper.eq(SystemUser::getId, id);
        // 设置 SET 子句：SET status = ?（将状态字段更新为传入的新值）
        updateWrapper.set(SystemUser::getStatus, status);
        // 执行更新，最终 SQL：UPDATE system_user SET status = ? WHERE id = ?
        service.update(updateWrapper);
        return Result.ok();
    }
}
