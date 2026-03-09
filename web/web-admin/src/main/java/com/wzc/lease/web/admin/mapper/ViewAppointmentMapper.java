package com.wzc.lease.web.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.lease.model.entity.ViewAppointment;
import com.wzc.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.wzc.lease.web.admin.vo.appointment.AppointmentVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 预约看房信息 Mapper 接口
 * <p>
 * 继承 MyBatis-Plus 的 BaseMapper，自动拥有对 view_appointment 表的基础 CRUD 操作
 * （如 insert、deleteById、updateById、selectById、selectList 等），无需手动编写 SQL。
 * <p>
 * 本接口额外声明了自定义查询方法，其 SQL 实现定义在对应的 XML 映射文件
 * （ViewAppointmentMapper.xml）中。
 *
 * @author liubo
 * @description 针对表【view_appointment(预约看房信息表)】的数据库操作Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.wzc.lease.model.ViewAppointment
 */
public interface ViewAppointmentMapper extends BaseMapper<ViewAppointment> {

    /**
     * 分页查询预约看房信息（自定义联表查询）
     * <p>
     * 联合 view_appointment 表和 apartment_info 表进行查询，
     * 支持按省份、城市、区域、公寓、姓名、手机号等条件动态筛选。
     * 具体 SQL 实现见 ViewAppointmentMapper.xml 中的 pageAppointment 语句。
     *
     * @param page    MyBatis-Plus 分页对象，方法的第一个参数为 Page 时，分页插件会自动生效
     * @param queryVo 查询条件封装对象
     * @return 分页结果（IPage），包含当前页数据和分页元信息
     */
    IPage<AppointmentVo> pageAppointment(Page<AppointmentVo> page, AppointmentQueryVo queryVo);
}
