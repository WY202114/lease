package com.wzc.lease.web.admin.controller.lease;


import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.enums.AppointmentStatus;
import com.wzc.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.wzc.lease.web.admin.vo.appointment.AppointmentVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


@Tag(name = "预约看房管理")
@RequestMapping("/admin/appointment") // 定义当前控制器的统一请求路径前缀
@RestController
public class ViewAppointmentController {

    @Operation(summary = "分页查询预约信息")
    @GetMapping("page") // 处理 HTTP GET 请求，常用于查询数据
    public Result<IPage<AppointmentVo>> page(@RequestParam long current, @RequestParam long size, AppointmentQueryVo queryVo) {
        return Result.ok();
    }

    @Operation(summary = "根据id更新预约状态")
    @PostMapping("updateStatusById") // 处理 HTTP POST 请求，执行状态更新动作
    public Result updateStatusById(@RequestParam Long id, @RequestParam AppointmentStatus status) {
        return Result.ok();
    }

}
