package com.wzc.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.lease.common.exception.LeaseException;
import com.wzc.lease.common.result.ResultCodeEnum;
import com.wzc.lease.model.entity.*;
import com.wzc.lease.model.enums.ItemType;
import com.wzc.lease.web.admin.mapper.*;
import com.wzc.lease.web.admin.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzc.lease.web.admin.vo.apartment.ApartmentDetailVo;
import com.wzc.lease.web.admin.vo.apartment.ApartmentItemVo;
import com.wzc.lease.web.admin.vo.apartment.ApartmentQueryVo;
import com.wzc.lease.web.admin.vo.apartment.ApartmentSubmitVo;
import com.wzc.lease.web.admin.vo.fee.FeeValueVo;
import com.wzc.lease.web.admin.vo.graph.GraphVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.events.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liubo
 * @description 针对表【apartment_info(公寓信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class ApartmentInfoServiceImpl extends ServiceImpl<ApartmentInfoMapper, ApartmentInfo>
        implements ApartmentInfoService {

    // 图片数据存在 graph_info 表
    // → GraphInfoService 负责 graph_info 表
    // → 所以删图片注入 graphInfoService
    @Autowired
    private GraphInfoService graphInfoService;

    @Autowired
    private ApartmentInfoMapper apartmentInfoMapper;// 这里报错不影响运行

    @Autowired
    private GraphInfoMapper graphInfoMapper;

    @Autowired
    private LabelInfoMapper labelInfoMapper;

    @Autowired
    private FacilityInfoMapper facilityInfoMapper;

    @Autowired
    private FeeValueMapper feeValueMapper;

    @Autowired
    private RoomInfoMapper roomInfoMapper;
    // 配套设施数据存在 apartment_facility 表
    // → ApartmentFacilityService 负责 apartment_facility 表
    // → 所以删配套注入 apartmentFacilityService
    @Autowired
    private ApartmentFacilityService apartmentService;

    @Autowired
    private ApartmentLabelService labelService;

    @Autowired
    private ApartmentFeeValueService feeValueService;

    @Override
    public void saveOrUpdateApartment(ApartmentSubmitVo apartmentSubmitVo) {
        // 【关键】必须在 saveOrUpdate 之前判断，原因：
        // MyBatis-Plus 的"主键回填"机制：
        // - 新增时：前端传来的对象没有ID（null），saveOrUpdate执行INSERT后，
        // 数据库生成的新ID会被自动塞回对象中，此时getId()不再是null
        // - 修改时：前端传来的对象本身就有ID，saveOrUpdate执行UPDATE，ID不变
        //
        // 如果先执行saveOrUpdate再判断：isUpdate永远是true（因为执行完后对象必然有ID）
        // 后续"先删后插"逻辑依赖这个值，所以必须在saveOrUpdate之前保存这段"原始状态"
        boolean isUpdate = apartmentSubmitVo.getId() != null;
        super.saveOrUpdate(apartmentSubmitVo);
        // 检查发来的是新增还是修改，有id就是修改，没id就是新增
        if (isUpdate) {
            // 【疑问解答】：为什么每一步的 eq 里面，都要写 entity::getApartmentId 和 apartmentSubmitVo.getId()？
            // 答：这是因为公寓（大哥）和图片/配套/标签等（小弟）是"一对多"的数据库表关系。
            // 所有的"小弟表"里都有一个专门的列（外键）叫 apartment_id，用来记录自己属于哪个公寓。
            // 这里的代码逻辑是"先删后插"（更新复杂列表属性的标准做法）：
            // 我们必须拿着当前正在修改的公寓ID（apartmentSubmitVo.getId()），
            // 去各个小弟表里，把所有认这个ID当大哥（getApartmentId）的旧数据全部揪出来删掉。

            // 1. 删除旧的图片列表
            // SQL 翻译：DELETE FROM graph_info WHERE item_type = 1 AND item_id = 当前公寓ID
            // 注意：图片表稍有不同，因为图片表可能是通用的，它用 item_type 区分是公寓图片还是房间图片，item_id 充当外键
            LambdaQueryWrapper<GraphInfo> graphQueryWrapper = new LambdaQueryWrapper<>();
            graphQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
            graphQueryWrapper.eq(GraphInfo::getItemId, apartmentSubmitVo.getId());
            graphInfoService.remove(graphQueryWrapper);

            // 2. 删除旧的配套列表
            // SQL 翻译：DELETE FROM apartment_facility WHERE apartment_id = 当前公寓ID
            LambdaQueryWrapper<ApartmentFacility> apartQueryWrapper = new LambdaQueryWrapper<>();
            apartQueryWrapper.eq(ApartmentFacility::getApartmentId, apartmentSubmitVo.getId());
            apartmentService.remove(apartQueryWrapper);

            // 3. 删除旧的标签列表
            // SQL 翻译：DELETE FROM apartment_label WHERE apartment_id = 当前公寓ID
            LambdaQueryWrapper<ApartmentLabel> labelQueryWrapper = new LambdaQueryWrapper<>();
            labelQueryWrapper.eq(ApartmentLabel::getApartmentId, apartmentSubmitVo.getId());
            labelService.remove(labelQueryWrapper);

            // 4. 删除旧的杂费列表
            // SQL 翻译：DELETE FROM apartment_fee_value WHERE apartment_id = 当前公寓ID
            LambdaQueryWrapper<ApartmentFeeValue> feeQueryWrapper = new LambdaQueryWrapper<>();
            feeQueryWrapper.eq(ApartmentFeeValue::getApartmentId, apartmentSubmitVo.getId());
            feeValueService.remove(feeQueryWrapper);
        }
        // 1.插入图片列表
        List<GraphVo> graphVoList = apartmentSubmitVo.getGraphVoList();
        ArrayList<GraphInfo> graphInfoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(graphVoList)) {// 判断图片列表不为null且不为空，避免无意义的遍历和数据库操作
            for (GraphVo graphVo : graphVoList) {
                GraphInfo graphInfo = new GraphInfo();
                // 设置图片归属类型：APARTMENT（因为这里是公寓图片，不是房间图片）
                graphInfo.setItemType(ItemType.APARTMENT);
                // 设置图片归属的公寓ID（saveOrUpdate之后ID已经回填进来了，所以能拿到）
                graphInfo.setItemId(apartmentSubmitVo.getId());
                // 从GraphVo里把name和url复制过来
                graphInfo.setName(graphVo.getName());
                graphInfo.setUrl(graphVo.getUrl());
                graphInfoList.add(graphInfo);
            }
            graphInfoService.saveBatch(graphInfoList);
        }
        // 2.插入配套列表
        // 2. 插入配套列表
        // 从前端提交的VO中取出配套设施ID列表（只有facilityId，没有apartmentId）
        List<Long> facilityInfoIdList = apartmentSubmitVo.getFacilityInfoIds();
        // 判断配套列表不为null且不为空，避免无意义的遍历和数据库操作
        if (!CollectionUtils.isEmpty(facilityInfoIdList)) {
            // 创建新集合，用来存转换后的ApartmentFacility对象（数据库关联表实体）
            ArrayList<ApartmentFacility> facilityList = new ArrayList<>();
            // 遍历每一个配套设施ID
            for (Long facilityId : facilityInfoIdList) {
                ApartmentFacility apartmentFacility = new ApartmentFacility();
                // 设置归属的公寓ID（saveOrUpdate之后ID已回填，所以能拿到）
                apartmentFacility.setApartmentId(apartmentSubmitVo.getId());
                // 设置配套设施ID
                apartmentFacility.setFacilityId(facilityId);
                facilityList.add(apartmentFacility);
            }

            // 批量插入所有配套关联记录到 apartment_facility 表
            apartmentService.saveBatch(facilityList);
        }
        // 3.插入标签列表
        List<Long> labelIds = apartmentSubmitVo.getLabelIds();
        if (!CollectionUtils.isEmpty(labelIds)) {
            List<ApartmentLabel> apartmentLabelList = new ArrayList<>();
            for (Long labelId : labelIds) {
                ApartmentLabel apartmentLabel = new ApartmentLabel();
                apartmentLabel.setApartmentId(apartmentSubmitVo.getId());
                apartmentLabel.setLabelId(labelId);
                apartmentLabelList.add(apartmentLabel);
            }
            labelService.saveBatch(apartmentLabelList);
        }

        // 4.插入杂费列表
        List<Long> feeValueIds = apartmentSubmitVo.getFeeValueIds();
        if (!CollectionUtils.isEmpty(feeValueIds)) {
            ArrayList<ApartmentFeeValue> apartmentFeeValueList = new ArrayList<>();
            for (Long feeValueId : feeValueIds) {
                ApartmentFeeValue apartmentFeeValue = new ApartmentFeeValue();
                /*
                 * @Builder注解作用：
                 * // 普通方式：必须先new一个空对象，再一行行set，字段越多越啰嗦
                 * // ApartmentFacility af = new ApartmentFacility();
                 * // af.setApartmentId(1L);
                 * // af.setFacilityId(2L);
                 *
                 * // @Builder方式：按需组装，想设置哪个写哪个，不需要的直接跳过，最后.build()一次性造出对象
                 * //ApartmentFacility af = ApartmentFacility.builder()
                 * //.apartmentId(1L)
                 * //.facilityId(2L)
                 * //.build();
                 */
                apartmentFeeValue.setApartmentId(apartmentSubmitVo.getId());
                apartmentFeeValue.setFeeValueId(feeValueId);
                apartmentFeeValueList.add(apartmentFeeValue);
            }
            feeValueService.saveBatch(apartmentFeeValueList);
        }

        // LambdaQueryWrapper 底层三步工作原理：
        // 第一步：解析方法引用，提取数据库列名
        // ApartmentInfo::getIsDeleted
        // → 反射拿到方法名：getIsDeleted
        // → 去掉get，转下划线：is_deleted（对应数据库列名）
        //
        // 第二步：每调用一次.eq()，就往内部集合添加一个条件
        // .eq(ApartmentInfo::getIsDeleted, 0) → {column: "is_deleted", value: 0}
        // .eq(ApartmentInfo::getRoomId, 101) → {column: "room_id", value: 101}
        // 内部集合：[ {is_deleted=0}, {room_id=101} ]
        //
        // 第三步：执行查询时，遍历集合拼接SQL
        // → WHERE is_deleted = 0 AND room_id = 101
        // LambdaQueryWrapper<ApartmentInfo> wrapper = new LambdaQueryWrapper<>();
        // wrapper.eq(ApartmentInfo::getIsDeleted, 0)
        // .eq(ApartmentInfo::getRoomId, 101);
        /*
         * 【知识点】：为什么刚才 new ApartmentLabel() 会报错？（Lombok 注解冲突问题）
         *
         * 1. Java 的默认规则：如果一个类没有任何构造方法，Java 编译器会自动送一个“无参构造方法”。有了它才能 new 对象。
         * 2. @Builder 的规则：当你在实体类上加了 @Builder 注解（为了使用链式调用构建对象），Lombok
         * 会自动为这个类生成一个“全参构造方法”。
         * 3. 两者冲突：Java 规定，只要你自己（或者框架）写了任何一个构造方法，Java 就不再免费送你“无参构造方法”了！
         *
         * 所以：加了 @Builder 的类，只有全参构造，没有无参构造。此时直接 new ApartmentLabel() 就会找不到方法而爆红。
         *
         * 【解决方案】（二选一）：
         * 方案一（推荐）：顺应框架，直接用 Builder 模式创建对象（更优雅，不用改实体类）。
         * ApartmentLabel label =
         * ApartmentLabel.builder().apartmentId(id).labelId(xx).build();
         * 方案二（你刚才用的）：强行把无参构造补回来。
         * 去实体类上加上 @NoArgsConstructor（强制生成无参构造，拯救 new 操作）。
         * 注意：加了无参必须同时加上 @AllArgsConstructor，否则 @Builder 会罢工报错。
         */
    }

    @Override
    public IPage<ApartmentItemVo> pageItem(Page<ApartmentItemVo> page, ApartmentQueryVo queryVo) {
        return apartmentInfoMapper.pageItem(page, queryVo);
    }

    @Override
    public ApartmentDetailVo getDetailById(Long id) {
        /*
         * 【VO 组装过程】
         * 以下每一步查询的返回类型，都严格对应 ApartmentDetailVo 中的字段类型。
         * 查出来的数据最终会 set 到 ApartmentDetailVo 的对应字段中，组装成完整对象返回给前端。
         */

        /*
         * 【以下每个方法的完整调用流程】（以 graphInfoMapper.selectByItemTypeAndId 为例，其他同理）：
         *
         * 1. Service 调用 Mapper 接口方法（如 graphInfoMapper.selectByItemTypeAndId(...)）
         * 2. MyBatis 通过 namespace + id 找到 XML 中对应的 SQL，把方法参数填入 #{} 占位符
         * 3. 数据库执行 SQL，返回结果集（类似 Excel 表格，每行一条数据）
         * 4. MyBatis 根据 resultType，对结果集的每一行自动执行：
         * - new 一个空对象（如 new GraphVo()）
         * - 将列名转驼峰后匹配属性名，调用 setter 赋值（如列名 name → setName("客厅图")）
         * - 将所有行的对象装入 List 并返回
         * 5. 返回的 List 赋值给左边的变量（如 graphVoList）
         *
         * 总结：你只需定义 SQL 和 resultType，MyBatis 自动完成"结果集 → Java 对象"的映射
         */

        // 1、查询公寓基本信息 → 对应 ApartmentDetailVo 继承的父类 ApartmentInfo
        ApartmentInfo apartmentInfo = apartmentInfoMapper.selectById(id);
        // 2、查询图片列表 → 对应 ApartmentDetailVo.graphVoList 字段（类型 List<GraphVo>）
        List<GraphVo> graphVoList = graphInfoMapper.selectByItemTypeAndId(ItemType.APARTMENT, id);
        // 3、查询标签列表 → 对应 ApartmentDetailVo.labelInfoList 字段（类型 List<LabelInfo>）
        List<LabelInfo> labelInfoList = labelInfoMapper.selectListByApartmentId(id);
        // 4、查询配套列表 → 对应 ApartmentDetailVo.facilityInfoList 字段（类型 List<FacilityInfo>）
        List<FacilityInfo> facilityInfoList = facilityInfoMapper.selectListByApartmentById(id);
        // 5、查询杂费列表 → 对应 ApartmentDetailVo.feeValueVoList 字段（类型 List<FeeValueVo>）
        List<FeeValueVo> feeValueVoList = feeValueMapper.selectListByApartmentById(id);
        // 6、组装结果 —— 把上面查到的"散装"数据，装进 ApartmentDetailVo 这个"快递包裹箱"中
        ApartmentDetailVo apartmentDetailVo = new ApartmentDetailVo();
        // 用 BeanUtils 一键复制父类 ApartmentInfo 的所有属性（id、name、phone 等十几个字段）
        // 相当于"复印机"：把 apartmentInfo（原件）的内容自动复印到 apartmentDetailVo（白纸）上
        // 原理：匹配同名的 getter/setter，例如 apartmentInfo.getName() →
        // apartmentDetailVo.setName()
        BeanUtils.copyProperties(apartmentInfo, apartmentDetailVo);
        // 手动 set 子类特有的 4 个列表字段（这些是 BeanUtils 复制不到的，因为源对象 ApartmentInfo 中没有这些字段）
        apartmentDetailVo.setGraphVoList(graphVoList); // 填入图片列表
        apartmentDetailVo.setLabelInfoList(labelInfoList); // 填入标签列表
        apartmentDetailVo.setFacilityInfoList(facilityInfoList); // 填入配套列表
        apartmentDetailVo.setFeeValueVoList(feeValueVoList); // 填入杂费列表
        return apartmentDetailVo; // 返回组装完毕的完整公寓详情对象给 Controller → 前端
    }

    @Override
    public void removeApartmentById(Long id) {

        /*
         * 你让助手去仓库数东西（selectCount），但你不能光说"去数一下"，你得先写一张纸条告诉他数什么：
         * 第 1 行：拿出一张空白纸条（new LambdaQueryWrapper）
         * 第 2 行：在纸条上写条件："只数 apartment_id = 当前公寓ID 的房间"（.eq(...)）
         * 第 3 行：把纸条交给助手，让他去数（selectCount(纸条)），助手回来告诉你数量（count）
         */
        LambdaQueryWrapper<RoomInfo> roomInfoQueryWrapper = new LambdaQueryWrapper<>();
        roomInfoQueryWrapper.eq(RoomInfo::getApartmentId, id);
        Long count = roomInfoMapper.selectCount(roomInfoQueryWrapper);
        if (count > 0) {
            // 配合LeaseException.java中public LeaseException(Integer code, String message)
            // throw new
            // LeaseException(ResultCodeEnum.ADMIN_APARTMENT_DELETE_ERROR.getCode(),ResultCodeEnum.ADMIN_APARTMENT_DELETE_ERROR.getMessage());
            // 另一中写法配合public LeaseException(ResultCodeEnum resultCodeEnum)
            throw new LeaseException(ResultCodeEnum.ADMIN_APARTMENT_DELETE_ERROR);// 只传入一个枚举实例
        }

        /*
         * 先删公寓本身有一个好处：
         * 想象你要拆一栋楼（公寓）和楼里的家具（关联数据）。
         * 先拆楼：楼没了，即使后面拆家具的过程中途出错，用户查询时也查不到这栋楼了，不会看到"残缺数据"
         * 先拆家具：如果拆到一半出错了，楼还在，用户查到的公寓会缺少部分关联数据（比如图片没了但公寓还在），体验更差
         */
        super.removeById(id);// apartment_info表删除

        // 1. 删除旧的图片列表
        // SQL 翻译：DELETE FROM graph_info WHERE item_type = 1 AND item_id = 当前公寓ID
        // 注意：图片表稍有不同，因为图片表可能是通用的，它用 item_type 区分是公寓图片还是房间图片，item_id 充当外键
        LambdaQueryWrapper<GraphInfo> graphQueryWrapper = new LambdaQueryWrapper<>();
        graphQueryWrapper.eq(GraphInfo::getItemType, ItemType.APARTMENT);
        graphQueryWrapper.eq(GraphInfo::getItemId, id);
        graphInfoService.remove(graphQueryWrapper);

        // 2. 删除旧的配套列表
        // SQL 翻译：DELETE FROM apartment_facility WHERE apartment_id = 当前公寓ID
        LambdaQueryWrapper<ApartmentFacility> apartQueryWrapper = new LambdaQueryWrapper<>();
        apartQueryWrapper.eq(ApartmentFacility::getApartmentId, id);
        apartmentService.remove(apartQueryWrapper);

        // 3. 删除旧的标签列表
        // SQL 翻译：DELETE FROM apartment_label WHERE apartment_id = 当前公寓ID
        LambdaQueryWrapper<ApartmentLabel> labelQueryWrapper = new LambdaQueryWrapper<>();
        labelQueryWrapper.eq(ApartmentLabel::getApartmentId, id);
        labelService.remove(labelQueryWrapper);

        // 4. 删除旧的杂费列表
        // SQL 翻译：DELETE FROM apartment_fee_value WHERE apartment_id = 当前公寓ID
        LambdaQueryWrapper<ApartmentFeeValue> feeQueryWrapper = new LambdaQueryWrapper<>();
        feeQueryWrapper.eq(ApartmentFeeValue::getApartmentId, id);
        feeValueService.remove(feeQueryWrapper);
    }
}
