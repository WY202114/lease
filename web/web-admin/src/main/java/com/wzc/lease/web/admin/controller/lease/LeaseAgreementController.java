package com.wzc.lease.web.admin.controller.lease;


import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.LeaseAgreement;
import com.wzc.lease.model.enums.LeaseStatus;
import com.wzc.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.wzc.lease.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


@Tag(name = "租约管理")
@RestController
@RequestMapping("/admin/agreement") // 定义当前控制器的统一请求路径前缀
public class LeaseAgreementController {

    @Operation(summary = "保存或修改租约信息")
    @PostMapping("saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdate(@RequestBody LeaseAgreement leaseAgreement) {
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询租约列表")
    @GetMapping("page") // 处理 HTTP GET 请求，常用于查询数据
    public Result<IPage<AgreementVo>> page(@RequestParam long current, @RequestParam long size, AgreementQueryVo queryVo) {
        return Result.ok();
    }

    @Operation(summary = "根据id查询租约信息")
    @GetMapping(name = "getById") // 处理 HTTP GET 请求，按ID查询详情
    public Result<AgreementVo> getById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "根据id删除租约信息")
    @DeleteMapping("removeById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result removeById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "根据id更新租约状态")
    @PostMapping("updateStatusById") // 处理 HTTP POST 请求，执行状态更新动作
    public Result updateStatusById(@RequestParam Long id, @RequestParam LeaseStatus status) {
        return Result.ok();
    }

}

