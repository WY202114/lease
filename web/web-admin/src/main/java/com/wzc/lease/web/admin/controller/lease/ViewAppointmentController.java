package com.wzc.lease.web.admin.controller.lease;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.ViewAppointment;
import com.wzc.lease.model.enums.AppointmentStatus;
import com.wzc.lease.web.admin.service.ViewAppointmentService;
import com.wzc.lease.web.admin.vo.appointment.AppointmentQueryVo;
import com.wzc.lease.web.admin.vo.appointment.AppointmentVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 预约看房管理控制器
 * <p>
 * 负责处理与"预约看房"相关的 HTTP 请求，包括：
 * 1. 分页查询预约信息（支持多条件筛选）
 * 2. 根据 ID 更新预约状态
 *
 * <h3>五个文件的调用逻辑（整体架构）</h3>
 * 
 * <pre>
 * 本模块遵循 Spring MVC + MyBatis-Plus 的经典三层架构，各层职责如下：
 *
 * ┌─────────────────────────────────────────────────────────────────────┐
 * │  1. Controller 层 (ViewAppointmentController.java)                │
 * │     - 接收 HTTP 请求，校验/解析请求参数                              │
 * │     - 调用 Service 层处理业务逻辑                                    │
 * │     - 将处理结果封装为统一响应对象 Result 返回给前端                    │
 * ├─────────────────────────────────────────────────────────────────────┤
 * │  2. Service 接口层 (ViewAppointmentService.java)                   │
 * │     - 定义业务方法的抽象接口                                         │
 * │     - 继承 IService，提供通用 CRUD 方法声明                          │
 * │     - Controller 依赖此接口（面向接口编程，降低耦合）                  │
 * ├─────────────────────────────────────────────────────────────────────┤
 * │  3. Service 实现层 (ViewAppointmentServiceImpl.java)               │
 * │     - 实现 Service 接口中定义的业务方法                               │
 * │     - 继承 ServiceImpl，获得通用 CRUD 的默认实现                      │
 * │     - 调用 Mapper 层执行数据库操作                                   │
 * ├─────────────────────────────────────────────────────────────────────┤
 * │  4. Mapper 接口层 (ViewAppointmentMapper.java)                     │
 * │     - 定义数据库操作方法的接口                                        │
 * │     - 继承 BaseMapper，自动拥有单表 CRUD 方法                        │
 * │     - 自定义方法的 SQL 写在对应的 XML 文件中                          │
 * ├─────────────────────────────────────────────────────────────────────┤
 * │  5. Mapper XML 层 (ViewAppointmentMapper.xml)                      │
 * │     - 编写自定义 SQL 语句（联表查询、动态条件等）                       │
 * │     - 定义 resultMap 处理复杂的结果集映射                             │
 * │     - 通过 namespace 与 Mapper 接口绑定                              │
 * └─────────────────────────────────────────────────────────────────────┘
 * </pre>
 *
 * <h3>请求链路一：分页查询预约信息 (GET /admin/appointment/page)</h3>
 * 
 * <pre>
 * 前端发起 GET 请求 ──▶ ViewAppointmentController.page()
 *     │  接收参数: current(页码), size(每页大小), AppointmentQueryVo(筛选条件)
 *     │  创建 Page 分页对象
 *     ▼
 * ViewAppointmentService.pageAppointment(page, queryVo)   [接口层]
 *     │  面向接口调用，Spring 注入的实际实例是 ServiceImpl
 *     ▼
 * ViewAppointmentServiceImpl.pageAppointment(page, queryVo) [实现层]
 *     │  将请求委托给 Mapper 层
 *     ▼
 * ViewAppointmentMapper.pageAppointment(page, queryVo)    [Mapper 接口]
 *     │  MyBatis 根据方法名匹配 XML 中的 SQL 语句
 *     │  分页插件自动拦截，在 SQL 末尾追加 LIMIT 子句
 *     ▼
 * ViewAppointmentMapper.xml → &lt;select id="pageAppointment"&gt;  [Mapper XML]
 *     │  执行联表查询: view_appointment LEFT JOIN apartment_info
 *     │  根据 queryVo 中非空字段动态拼接 WHERE 条件
 *     │  通过 resultMap 将结果映射为 AppointmentVo 对象
 *     ▼
 * 返回 IPage&lt;AppointmentVo&gt; → 封装为 Result.ok() → 返回 JSON 给前端
 * </pre>
 *
 * <h3>请求链路二：更新预约状态 (POST /admin/appointment/updateStatusById)</h3>
 * 
 * <pre>
 * 前端发起 POST 请求 ──▶ ViewAppointmentController.updateStatusById()
 *     │  接收参数: id(预约ID), status(目标状态)
 *     │  使用 LambdaUpdateWrapper 构建更新条件:
 *     │    .eq(ViewAppointment::getId, id)                → WHERE id = ?
 *     │    .set(ViewAppointment::getAppointmentStatus, status) → SET appointment_status = ?
 *     ▼
 * ViewAppointmentService.update(wrapper)   [继承自 IService 的通用方法]
 *     │  无需在 Service 接口中额外定义，IService 已提供
 *     ▼
 * ViewAppointmentServiceImpl.update(wrapper) [继承自 ServiceImpl 的默认实现]
 *     │  无需手动编写，ServiceImpl 已实现，内部调用 Mapper
 *     ▼
 * ViewAppointmentMapper.update(null, wrapper) [继承自 BaseMapper 的通用方法]
 *     │  无需编写 XML，MyBatis-Plus 自动生成 SQL:
 *     │  UPDATE view_appointment SET appointment_status = ? WHERE id = ?
 *     ▼
 * 数据库执行更新 → 返回 Result.ok() → 返回 JSON 给前端
 * </pre>
 */
@Tag(name = "预约看房管理") // Swagger 标签，用于对 API 进行分组展示
@RequestMapping("/admin/appointment") // 定义当前控制器的统一请求路径前缀
@RestController // 标记为 RESTful 控制器，方法返回值自动序列化为 JSON
public class ViewAppointmentController {

    @Autowired // 通过 Spring 依赖注入获取预约看房服务层实例
    private ViewAppointmentService service;

    /**
     * 分页查询预约信息
     *
     * @param current 当前页码（从 1 开始）
     * @param size    每页显示的记录数
     * @param queryVo 查询条件对象，包含省份、城市、区域、公寓、姓名、手机号等筛选条件
     * @return 包含分页结果的统一响应对象，数据为 AppointmentVo 分页列表
     */
    @Operation(summary = "分页查询预约信息") // Swagger 接口描述
    @GetMapping("page") // 处理 HTTP GET 请求，常用于查询数据
    public Result<IPage<AppointmentVo>> page(@RequestParam long current, @RequestParam long size,
            AppointmentQueryVo queryVo) {
        // 创建分页对象，设置当前页码和每页大小
        Page<AppointmentVo> page = new Page<>();
        // 调用 Service 层进行分页查询，底层会通过 Mapper 执行自定义 SQL
        IPage<AppointmentVo> result = service.pageAppointment(page, queryVo);
        return Result.ok(result);
    }

    /**
     * 根据 ID 更新预约状态
     * <p>
     * 通过 LambdaUpdateWrapper 构建更新条件，只修改指定 ID 记录的预约状态字段，
     * 避免了全字段更新，提高了安全性和效率。
     *
     * @param id     预约记录的主键 ID
     * @param status 要更新的目标状态（枚举类型：待看房、已看房、已取消等）
     * @return 统一响应对象，表示操作是否成功
     */
    @Operation(summary = "根据id更新预约状态") // Swagger 接口描述
    @PostMapping("updateStatusById") // 处理 HTTP POST 请求，执行状态更新动作
    public Result updateStatusById(@RequestParam Long id, @RequestParam AppointmentStatus status) {
        // 创建 LambdaUpdateWrapper，用于以类型安全的方式构建 UPDATE 语句的条件和赋值
        LambdaUpdateWrapper<ViewAppointment> viewUpdateWrapper = new LambdaUpdateWrapper<>();
        // 设置 WHERE 条件：id = 传入的 id 值
        viewUpdateWrapper.eq(ViewAppointment::getId, id);
        // 设置 SET 赋值：appointment_status = 传入的 status 值
        viewUpdateWrapper.set(ViewAppointment::getAppointmentStatus, status);
        // 执行更新操作，生成的 SQL 类似：UPDATE view_appointment SET appointment_status = ? WHERE id
        // = ?
        service.update(viewUpdateWrapper);
        return Result.ok();
    }

}
