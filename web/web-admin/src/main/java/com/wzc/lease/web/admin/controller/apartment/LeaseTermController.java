package com.wzc.lease.web.admin.controller.apartment;

import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.LeaseTerm;
import com.wzc.lease.web.admin.service.LeaseTermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "租期管理")
@RequestMapping("/admin/term") // 定义当前控制器的统一请求路径前缀
@RestController
public class LeaseTermController {

    // @Autowired 是 Spring 框架的注解，它的作用是"依赖注入"
    // 简单来说：Spring 容器启动时会自动帮你 new 一个 LeaseTermService 的实现类对象（LeaseTermServiceImpl），
    // 并自动赋值给下面这个 service 变量，这样你就可以直接调用 service 里的方法了，不用自己手动去 new
    @Autowired
    private LeaseTermService service;

    @GetMapping("list") // 处理 HTTP GET 请求，常用于查询数据
    @Operation(summary = "查询全部租期列表")
    public Result<List<LeaseTerm>> listLeaseTerm() {
        // 调用 MyBatis-Plus 提供的 list() 方法，它会自动执行 SELECT * FROM lease_term WHERE
        // is_deleted = 0
        // 将查询到的所有租期数据，封装成一个 List 集合返回
        List<LeaseTerm> list = service.list();

        // Result 是我们自己封装的统一返回结果类（包含 code、message、data）
        // Result.ok(list) 表示请求成功（code=200），并把查询到的 list 数据装到 data 属性中返回给前端前端
        return Result.ok(list);
    }

    @PostMapping("saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    @Operation(summary = "保存或更新租期信息")
    public Result saveOrUpdate(@RequestBody LeaseTerm leaseTerm) {
        // 【这句话到底在干嘛？】
        // 这句话就是指挥 service 帮我们去操作数据库：
        // "service 兄弟，请把前端传过来的 leaseTerm（租期数据）保存到数据库里！"
        // (至于它是新增还是修改，service 内部会自动根据 id 存不存在去判断)
        service.saveOrUpdate(leaseTerm);
        // Result.ok() 没有传参数，表示只给前端返回请求成功的状态码（code=200）和消息（message="成功"），不需要返回具体数据
        return Result.ok();
    }

    @DeleteMapping("deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    @Operation(summary = "根据ID删除租期")
    public Result deleteLeaseTermById(@RequestParam Long id) {
        // 【这句话到底在干嘛？】
        // 这句话就是指挥 service 帮我们去操作数据库：
        // "service 兄弟，请去数据库把 主键id为这个数 的那条数据删掉！"
        // (注：因为配了 @TableLogic，所以它实际上是去数据库把 is_deleted 改成了 1)
        service.removeById(id);
        // 同上，只告诉前端"删除成功"即可，不需要返回什么数据
        return Result.ok();
    }
}
