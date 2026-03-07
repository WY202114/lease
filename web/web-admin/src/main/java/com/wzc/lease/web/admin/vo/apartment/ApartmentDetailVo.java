package com.wzc.lease.web.admin.vo.apartment;

import com.wzc.lease.model.entity.ApartmentInfo;
import com.wzc.lease.model.entity.FacilityInfo;
import com.wzc.lease.model.entity.LabelInfo;
import com.wzc.lease.web.admin.vo.graph.GraphVo;
import com.wzc.lease.web.admin.vo.fee.FeeValueVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "公寓信息")
@Data
/*
 * 【VO 组装容器】
 * ApartmentDetailVo 是返回给前端的"公寓详情"数据模型。
 * 它继承了 ApartmentInfo（公寓基本信息），并额外定义了 4 个列表字段。
 * 在 Service 层（ApartmentInfoServiceImpl.getDetailById）中，
 * 会分别调用不同的 Mapper 查询数据，然后逐一填充到这 4 个字段中，最终返回给前端。
 * 每个字段的类型决定了对应 Mapper 方法的返回类型必须与之一致，否则无法 set 进来。
 */
public class ApartmentDetailVo extends ApartmentInfo {

    @Schema(description = "图片列表")
    // 数据来源：graphInfoMapper.selectByItemTypeAndId() → 返回 List<GraphVo>
    private List<GraphVo> graphVoList;

    @Schema(description = "标签列表")
    // 数据来源：labelInfoMapper.selectListByApartmentId() → 返回 List<LabelInfo>
    private List<LabelInfo> labelInfoList;

    @Schema(description = "配套列表")
    // 数据来源：facilityInfoMapper.selectListByApartmentById() → 返回 List<FacilityInfo>
    private List<FacilityInfo> facilityInfoList;

    @Schema(description = "杂费列表")
    // 数据来源：feeValueMapper.selectListByApartmentById() → 返回 List<FeeValueVo>
    private List<FeeValueVo> feeValueVoList;

}
