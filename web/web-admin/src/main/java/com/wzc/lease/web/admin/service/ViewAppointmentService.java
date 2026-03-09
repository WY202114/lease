package com.wzc.lease.web.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.lease.model.entity.ViewAppointment;
import com.wzc.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.wzc.lease.web.admin.vo.appointment.AppointmentVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 预约看房信息 Service 接口
 * <p>
 * 继承了 MyBatis-Plus 的 IService 接口，自动拥有常用的 CRUD 方法（如 save、remove、update、get、list
 * 等），
 * 无需手动编写基础的增删改查逻辑。
 * <p>
 * 本接口额外定义的方法用于满足自定义的业务查询需求（如分页+多条件联表查询）。
 *
 * @author liubo
 * @description 针对表【view_appointment(预约看房信息表)】的数据库操作Service
 * @createDate 2023-07-24 15:48:00
 */
public interface ViewAppointmentService extends IService<ViewAppointment> {

    /**
     * 分页查询预约看房信息（自定义联表查询）
     * <p>
     * 该方法会联合查询 view_appointment 表和 apartment_info 表，
     * 返回包含公寓详细信息的预约数据。支持按省份、城市、区域、公寓、姓名、手机号等条件筛选。
     *
     * @param page    MyBatis-Plus 分页对象，框架会自动处理分页参数（LIMIT / OFFSET）
     * @param queryVo 查询条件封装对象，包含各筛选字段（为 null 时表示不过滤该条件）
     * @return 分页结果，包含当前页数据列表和分页元信息（总记录数、总页数等）
     */
    IPage<AppointmentVo> pageAppointment(Page<AppointmentVo> page, AppointmentQueryVo queryVo);
}
