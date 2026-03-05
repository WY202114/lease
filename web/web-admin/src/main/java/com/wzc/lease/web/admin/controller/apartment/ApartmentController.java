package com.wzc.lease.web.admin.controller.apartment;

import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.ApartmentInfo;
import com.wzc.lease.model.enums.ReleaseStatus;
import com.wzc.lease.web.admin.service.ApartmentInfoService;
import com.wzc.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.wzc.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.wzc.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.wzc.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "公寓信息管理")
@RestController
@RequestMapping("/admin/apartment") // 定义当前控制器的统一请求路径前缀
public class ApartmentController {

    @Autowired
    private ApartmentInfoService service;

    @Operation(summary = "保存或更新公寓信息")
    @PostMapping("saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdate(@RequestBody ApartmentSubmitVo apartmentSubmitVo) {
        //service.saveOrUpdate(apartmentSubmitVo);// 这里的service是ApartmentInfoService的实例，只和ApartmentInfo表打交道，而ApartmentSubmitVo还和其他表打交道，所以需要单独创建一个方法
        service.saveOrUpdateApartment(apartmentSubmitVo);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询公寓列表")
    @GetMapping("pageItem") // 处理 HTTP GET 请求，常用于查询数据
    public Result<IPage<ApartmentItemVo>> pageItem(@RequestParam long current, @RequestParam long size,
            ApartmentQueryVo queryVo) {
        return Result.ok();
    }

    @Operation(summary = "根据ID获取公寓详细信息")
    @GetMapping("getDetailById") // 处理 HTTP GET 请求，按ID查询详情
    public Result<ApartmentDetailVo> getDetailById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "根据id删除公寓信息")
    @DeleteMapping("removeById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result removeById(@RequestParam Long id) {
        return Result.ok();
    }

    @Operation(summary = "根据id修改公寓发布状态")
    @PostMapping("updateReleaseStatusById") // 处理 HTTP POST 请求，执行状态更新动作
    public Result updateReleaseStatusById(@RequestParam Long id, @RequestParam ReleaseStatus status) {
        return Result.ok();
    }

    @Operation(summary = "根据区县id查询公寓信息列表")
    @GetMapping("listInfoByDistrictId") // 处理 HTTP GET 请求，按条件查询列表
    public Result<List<ApartmentInfo>> listInfoByDistrictId(@RequestParam Long id) {
        return Result.ok();
    }
}
