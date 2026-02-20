package com.wzc.lease.web.admin.mapper; // 把该接口归类到管理端 mapper 包，便于统一扫描与注入

import com.wzc.lease.model.entity.ApartmentFacility; // 指定当前 mapper 处理的数据实体类型（公寓与配套关系）
import com.baomidou.mybatisplus.core.mapper.BaseMapper; // 复用 MyBatis-Plus 通用 CRUD 能力，减少手写 SQL

/**
 * @author liubo
 * @description 针对表【apartment_facility(公寓&配套关联表)】的数据库操作 Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.wzc.lease.model.entity.ApartmentFacility
 */
public interface ApartmentFacilityMapper extends BaseMapper<ApartmentFacility> { // 通过泛型绑定实体后自动获得该表的基础增删改查能力

} // 保持空接口即可，后续如有复杂查询再在这里补充自定义方法签名
