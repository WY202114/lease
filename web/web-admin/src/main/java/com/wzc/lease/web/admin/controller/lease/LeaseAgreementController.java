package com.wzc.lease.web.admin.controller.lease;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.LeaseAgreement;
import com.wzc.lease.model.enums.LeaseStatus;
import com.wzc.lease.web.admin.service.LeaseAgreementService;
import com.wzc.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.wzc.lease.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.web.bind.annotation.*;

@Tag(name = "租约管理")
@RestController
@RequestMapping("/admin/agreement") // 定义当前控制器的统一请求路径前缀
public class LeaseAgreementController {

    @Autowired
    private LeaseAgreementService service;

    @Operation(summary = "保存或修改租约信息")
    @PostMapping("saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdate(@RequestBody LeaseAgreement leaseAgreement) {
        // 只涉及一张表，使用通用service中的saveOrUpdate
        service.saveOrUpdate(leaseAgreement);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询租约列表")
    @GetMapping("page") // 处理 HTTP GET 请求，常用于查询数据
    public Result<IPage<AgreementVo>> page(@RequestParam long current, @RequestParam long size,
            AgreementQueryVo queryVo) {
        // 创建分页对象，current = 当前页码，size = 每页条数
        // MyBatis-Plus 的分页插件会自动根据这两个参数计算偏移量并拼接 LIMIT 子句：
        // offset = (current - 1) × size
        // 例如：current=2, size=10 → offset=(2-1)×10=10 → LIMIT 10, 10（跳过前10条，取10条）
        // current=1, size=10 → LIMIT 0, 10（取第1~10条）
        // current=3, size=10 → LIMIT 20, 10（取第21~30条）
        Page<AgreementVo> page = new Page<>(current, size);
        /*
         * 前端发出的请求就是：
         * 
         * GET
         * /admin/agreement/page?current=1&size=10&provinceId=110000&apartmentId=5&phone
         * =13812345678
         * Spring 是怎么接收的？
         * java
         * public Result<IPage<AgreementVo>> page(
         * 
         * @RequestParam long current, // ← 接收 current=1
         * 
         * @RequestParam long size, // ← 接收 size=10
         * AgreementQueryVo queryVo // ← Spring 自动把剩余参数装进这个对象
         * )
         * 
         * // Spring 内部自动执行的逻辑（你看不到，但实际就是这么做的）：
         * AgreementQueryVo queryVo = new AgreementQueryVo(); // 1. 先创建空对象
         * queryVo.setProvinceId(110000L); // 2. URL 里有 provinceId=110000，调用 set 填入
         * queryVo.setApartmentId(5L); // 3. URL 里有 apartmentId=5，调用 set 填入
         * queryVo.setPhone("13812345678"); // 4. URL 里有 phone=13812345678，调用 set 填入
         * // cityId、districtId、roomNumber、name → URL 里没传，不调用 set，保持默认值 null
         */
        IPage<AgreementVo> result = service.pageAgreement(page, queryVo);
        return Result.ok(result);
    }

    @Operation(summary = "根据id查询租约信息")
    @GetMapping(name = "getById") // 处理 HTTP GET 请求，按ID查询详情
    public Result<AgreementVo> getById(@RequestParam Long id) {
        // 涉及多表查询
        AgreementVo result = service.getAgreementById(id);
        return Result.ok(result);
    }

    @Operation(summary = "根据id删除租约信息")
    @DeleteMapping("removeById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result removeById(@RequestParam Long id) {
        service.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "根据id更新租约状态")
    @PostMapping("updateStatusById") // 处理 HTTP POST 请求，执行状态更新动作
    public Result updateStatusById(@RequestParam Long id, @RequestParam LeaseStatus status) {
        LambdaUpdateWrapper<LeaseAgreement> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(LeaseAgreement::getId, id);
        updateWrapper.set(LeaseAgreement::getStatus, status);
        service.update(updateWrapper);
        return Result.ok();
    }

}
