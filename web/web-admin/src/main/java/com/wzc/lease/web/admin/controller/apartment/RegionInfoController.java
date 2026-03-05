package com.wzc.lease.web.admin.controller.apartment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.CityInfo;
import com.wzc.lease.model.entity.DistrictInfo;
import com.wzc.lease.model.entity.ProvinceInfo;
import com.wzc.lease.web.admin.service.CityInfoService;
import com.wzc.lease.web.admin.service.DistrictInfoService;
import com.wzc.lease.web.admin.service.ProvinceInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "地区信息管理")
@RestController
@RequestMapping("/admin/region") // 定义当前控制器的统一请求路径前缀
public class RegionInfoController {

    @Autowired
    private ProvinceInfoService provinceInfoService;
    @Autowired
    private CityInfoService cityInfoService;
    @Autowired
    private DistrictInfoService districtInfoService;

    @Operation(summary = "查询省份信息列表")
    @GetMapping("province/list") // 处理 HTTP GET 请求，常用于查询数据
    public Result<List<ProvinceInfo>> listProvince() {
        List<ProvinceInfo> list = provinceInfoService.list();
        return Result.ok(list);
    }

    @Operation(summary = "根据省份id查询城市信息列表")
    @GetMapping("city/listByProvinceId") // 处理 HTTP GET 请求，按条件查询列表
    // 这里的参数 id 代表的是“省份的ID”，前端传过来的是省份编号
    public Result<List<CityInfo>> listCityInfoByProvinceId(@RequestParam Long id) {
        LambdaQueryWrapper<CityInfo> queryWrapper = new LambdaQueryWrapper<>();
        // 拿着省份ID，去城市表(city_info)里找：哪些城市的 province_id(所属省份) 等于这个 id
        // 等价 SQL: WHERE province_id = #{id}
        queryWrapper.eq(CityInfo::getProvinceId, id);
        List<CityInfo> list = cityInfoService.list(queryWrapper);
        return Result.ok(list);
    }

    @GetMapping("district/listByCityId") // 处理 HTTP GET 请求，按条件查询列表
    @Operation(summary = "根据城市id查询区县信息")
    // 这里的参数 id 代表的是“城市的ID”，前端传过来的是城市编号
    public Result<List<DistrictInfo>> listDistrictInfoByCityId(@RequestParam Long id) {
        LambdaQueryWrapper<DistrictInfo> queryWrapper = new LambdaQueryWrapper<>();
        // 拿着城市ID，去区县表(district_info)里找：哪些区县的 city_id(所属城市) 等于这个 id
        // 等价 SQL: WHERE city_id = #{id}
        // 虽然两个方法的参数都叫 id，但它们代表的父级不同：上面是省的ID，这里是市的ID
        queryWrapper.eq(DistrictInfo::getCityId, id);
        List<DistrictInfo> list = districtInfoService.list(queryWrapper);
        return Result.ok(list);
    }

}
