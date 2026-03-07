package com.wzc.lease.web.admin.vo.apartment;

import com.wzc.lease.model.entity.ApartmentInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "后台管理系统公寓列表实体")
public class ApartmentItemVo extends ApartmentInfo {

    @Schema(description = "房间总数")//从room_info表中count计算
    private Long totalRoomCount;

    @Schema(description = "空闲房间数")//利用房间总数-不空闲房间总数
    private Long freeRoomCount;

}
