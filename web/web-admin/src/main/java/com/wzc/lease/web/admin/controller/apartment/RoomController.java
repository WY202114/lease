package com.wzc.lease.web.admin.controller.apartment;


import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.RoomInfo;
import com.wzc.lease.model.enums.ReleaseStatus;
import com.wzc.lease.web.admin.vo.room.RoomDetailVo;
import com.wzc.lease.web.admin.vo.room.RoomItemVo;
import com.wzc.lease.web.admin.vo.room.RoomQueryVo;
import com.wzc.lease.web.admin.vo.room.RoomSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "房间信息管理")
@RestController
@RequestMapping("/admin/room") // 定义当前控制器的统一请求路径前缀
public class RoomController {

    @Operation(summary = "保存或更新房间信息")
    @PostMapping("saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdate(@RequestBody RoomSubmitVo roomSubmitVo) {
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询房间列表")
    @GetMapping("pageItem") // 处理 HTTP GET 请求，常用于查询数据
    public Result<IPage<RoomItemVo>> pageItem(@RequestParam long current, @RequestParam long size, RoomQueryVo queryVo) {
        return Result.ok();
    }

    @Operation(summary = "根据id获取房间详细信息")
    @GetMapping("getDetailById") // 处理 HTTP GET 请求，按ID查询详情
    public Result<RoomDetailVo> getDetailById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "根据id删除房间信息")
    @DeleteMapping("removeById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result removeById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "根据id修改房间发布状态")
    @PostMapping("updateReleaseStatusById") // 处理 HTTP POST 请求，执行状态更新动作
    public Result updateReleaseStatusById(Long id, ReleaseStatus status) {
        return Result.ok();
    }

    @GetMapping("listBasicByApartmentId") // 处理 HTTP GET 请求，按条件查询列表
    @Operation(summary = "根据公寓id查询房间列表")
    public Result<List<RoomInfo>> listBasicByApartmentId(Long id) {
        return Result.ok();
    }

}


















