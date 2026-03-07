package com.wzc.lease.web.admin.controller.apartment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        // service.saveOrUpdate(apartmentSubmitVo);//
        // 这里的service是ApartmentInfoService的实例，只和ApartmentInfo表打交道，而ApartmentSubmitVo还和其他表打交道，所以需要单独创建一个方法
        service.saveOrUpdateApartment(apartmentSubmitVo);
        return Result.ok();
    }

    @Operation(summary = "根据条件分页查询公寓列表")
    @GetMapping("pageItem") // 处理 HTTP GET 请求，常用于查询数据
    public Result<IPage<ApartmentItemVo>> pageItem(@RequestParam long current, @RequestParam long size,
            ApartmentQueryVo queryVo) {
        Page<ApartmentItemVo> page = new Page<>(current, size);
        IPage<ApartmentItemVo> result = service.pageItem(page, queryVo);
        return Result.ok(result);
    }

    @Operation(summary = "根据ID获取公寓详细信息")
    @GetMapping("getDetailById") // 处理 HTTP GET 请求，按ID查询详情
    public Result<ApartmentDetailVo> getDetailById(@RequestParam Long id) {
        ApartmentDetailVo result = service.getDetailById(id);
        return Result.ok(result);
    }

    @Operation(summary = "根据id删除公寓信息")
    @DeleteMapping("removeById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result removeById(@RequestParam Long id) {
        // service.removeById(id);
        // 删公寓不只是删 apartment_info 一张表
        // service.removeById(id) 是 MyBatis-Plus 内置的方法，它只会删 apartment_info 表中的那一条记录。
        /*
         * 删除公寓 ID=1 时，需要同时删除：
         * ├── apartment_info 表中 id=1 的记录 ← removeById 只删这个
         * ├── graph_info 表中该公寓的图片 ← 没删！遗留垃圾数据
         * ├── apartment_label 表中该公寓的标签关联 ← 没删！
         * ├── apartment_facility 表中该公寓的配套关联 ← 没删！
         * └── apartment_fee_value 表中该公寓的杂费关联 ← 没删！
         */
        service.removeApartmentById(id);
        return Result.ok();
    }

    /*
     * LambdaQueryWrapper = 只拼 WHERE 条件 → 给 查询 / 删除 用
     * LambdaUpdateWrapper = 拼 WHERE 条件 + SET 修改值 → 专门给 修改 用
     */
    @Operation(summary = "根据id修改公寓发布状态")
    @PostMapping("updateReleaseStatusById") // 处理 HTTP POST 请求，执行状态更新动作
    public Result updateReleaseStatusById(@RequestParam Long id, @RequestParam ReleaseStatus status) {
        LambdaUpdateWrapper<ApartmentInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ApartmentInfo::getId, id); // → 生成 WHERE 部分：WHERE id = #{id}
        updateWrapper.set(ApartmentInfo::getIsRelease, status); // → 生成 SET 部分：SET is_release = #{status}
        // 最终拼成完整 SQL：UPDATE apartment_info SET is_release = #{status} WHERE id = #{id}
        service.update(updateWrapper);// service.update(updateWrapper) 是 MyBatis-Plus 内置的方法。
        /*
         * update方法就像一个只会执行命令的机器人，你不能光跟它说"去改数据"，你得给它一张指令单（updateWrapper），上面写清楚：
         * 找谁：WHERE id = 9
         * 改啥：SET is_release = 1
         * 它拿到指令单后，拼成完整 SQL → 交给数据库执行
         */
        return Result.ok();
    }

    @Operation(summary = "根据区县id查询公寓信息列表")
    @GetMapping("listInfoByDistrictId") // 处理 HTTP GET 请求，按条件查询列表
    public Result<List<ApartmentInfo>> listInfoByDistrictId(@RequestParam Long id) {
        LambdaQueryWrapper<ApartmentInfo> apartmentQueryWrapper = new LambdaQueryWrapper<>();
        apartmentQueryWrapper.eq(ApartmentInfo::getDistrictId, id);
        List<ApartmentInfo> list = service.list(apartmentQueryWrapper);
        return Result.ok(list );
    }
}
