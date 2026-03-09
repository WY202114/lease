package com.wzc.lease.web.admin.controller.system;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.SystemPost;
import com.wzc.lease.model.enums.BaseStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzc.lease.web.admin.service.SystemPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台用户岗位管理 控制器
 * 提供岗位信息的分页查询、增删改查以及状态相关的接口。
 */
@RestController
@Tag(name = "后台用户岗位管理")
@RequestMapping("/admin/system/post") // 定义当前控制器的统一请求路径前缀
public class SystemPostController {// 该岗位管理只涉及单表操作

    @Autowired
    private SystemPostService service;

    /**
     * 分页获取岗位信息
     * 
     * @param current 当前页码
     * @param size    每页显示条数
     * @return 包含岗位分页数据的返回对象
     */
    @Operation(summary = "分页获取岗位信息")
    @GetMapping("page") // 处理 HTTP GET 请求，常用于查询数据
    private Result<IPage<SystemPost>> page(@RequestParam long current, @RequestParam long size) {
        Page<SystemPost> page = new Page<>(current, size);
        Page<SystemPost> page1 = service.page(page);
        return Result.ok(page1);
    }

    /**
     * 保存或更新岗位信息
     * 如果传入的岗位对象包含 ID，则执行更新操作；如果没有 ID，则执行新增操作。
     * 
     * @param systemPost 岗位信息的实体对象
     * @return 成功操作的响应结果
     */
    @Operation(summary = "保存或更新岗位信息")
    @PostMapping("saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdate(@RequestBody SystemPost systemPost) {
        service.saveOrUpdate(systemPost);
        return Result.ok();
    }

    /**
     * 根据岗位 ID 删除岗位记录
     * 
     * @param id 岗位的唯一标识 (主键)
     * @return 成功操作的响应结果
     */
    @DeleteMapping("deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    @Operation(summary = "根据id删除岗位")
    public Result removeById(@RequestParam Long id) {
        service.removeById(id);
        return Result.ok();
    }

    /**
     * 根据 ID 获取岗位详细信息
     * 
     * @param id 岗位的唯一标识
     * @return 包含对应岗位详细信息的返回结果
     */
    @GetMapping("getById") // 处理 HTTP GET 请求，按ID查询详情
    @Operation(summary = "根据id获取岗位信息")
    public Result<SystemPost> getById(@RequestParam Long id) {
        SystemPost systemPost = service.getById(id);
        return Result.ok(systemPost);
    }

    /**
     * 获取全部岗位的列表数据
     * 
     * @return 包含所有岗位实体列表的返回结果
     */
    @Operation(summary = "获取全部岗位列表")
    @GetMapping("list") // 处理 HTTP GET 请求，常用于查询列表
    public Result<List<SystemPost>> list() {
        List<SystemPost> list = service.list();
        return Result.ok(list);
    }

    /**
     * 根据岗位 ID 更改当前岗位状态
     * 
     * @param id     岗位的唯一标识
     * @param status 新的岗位状态（例如启用、停用）
     * @return 成功操作的响应结果
     */
    @Operation(summary = "根据岗位id修改状态")
    @PostMapping("updateStatusByPostId") // 处理 HTTP POST 请求，执行状态更新动作
    public Result updateStatusByPostId(@RequestParam Long id, @RequestParam BaseStatus status) {
        // 创建 LambdaUpdateWrapper 条件构造器，用于构建 UPDATE 语句的 SET 和 WHERE 部分
        LambdaUpdateWrapper<SystemPost> updateWrapper = new LambdaUpdateWrapper<>();
        // WHERE 条件：匹配 id 字段等于传入的岗位 id（即 WHERE id = ?）
        updateWrapper.eq(SystemPost::getId, id);
        // SET 子句：将 status 字段设置为传入的新状态值（即 SET status = ?）
        updateWrapper.set(SystemPost::getStatus, status);
        // 执行更新操作，最终生成的 SQL 类似：UPDATE system_post SET status = ? WHERE id = ?
        service.update(updateWrapper);
        return Result.ok();
    }
}
