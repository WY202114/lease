package com.wzc.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.lease.model.entity.ViewAppointment;
import com.wzc.lease.web.admin.mapper.ViewAppointmentMapper;
import com.wzc.lease.web.admin.service.ViewAppointmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzc.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.wzc.lease.web.admin.vo.appointment.AppointmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 预约看房信息 Service 实现类
 * <p>
 * 继承 ServiceImpl，自动获得 MyBatis-Plus 提供的通用 CRUD 实现。
 * ServiceImpl 的两个泛型参数含义：
 * - ViewAppointmentMapper：指定该 Service 使用的 Mapper 接口
 * - ViewAppointment：指定该 Service 操作的实体类
 * <p>
 * 同时实现 ViewAppointmentService 接口，提供自定义业务方法的具体实现。
 *
 * @author liubo
 * @description 针对表【view_appointment(预约看房信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service // 标记为 Spring Service 组件，交由 Spring 容器管理（自动注册为 Bean）
public class ViewAppointmentServiceImpl extends ServiceImpl<ViewAppointmentMapper, ViewAppointment>
        implements ViewAppointmentService {

    @Autowired // 注入 Mapper 层，用于调用自定义的 SQL 查询方法
    private ViewAppointmentMapper viewAppointmentMapper;

    /**
     * 分页查询预约看房信息
     * <p>
     * 将分页对象和查询条件委托给 Mapper 层执行自定义 SQL 查询。
     * MyBatis-Plus 的分页插件会自动拦截该查询，在 SQL 末尾追加 LIMIT 子句实现物理分页。
     *
     * @param page    分页对象，包含页码和每页大小
     * @param queryVo 查询条件对象
     * @return 分页查询结果
     */
    @Override
    public IPage<AppointmentVo> pageAppointment(Page<AppointmentVo> page, AppointmentQueryVo queryVo) {
        // 调用 Mapper 的自定义查询方法，分页参数由 MyBatis-Plus 分页插件自动处理
        return viewAppointmentMapper.pageAppointment(page, queryVo);
    }
}
