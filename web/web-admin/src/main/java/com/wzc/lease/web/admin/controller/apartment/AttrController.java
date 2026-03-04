package com.wzc.lease.web.admin.controller.apartment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzc.lease.common.result.Result;
import com.wzc.lease.model.entity.AttrKey;
import com.wzc.lease.model.entity.AttrValue;
import com.wzc.lease.web.admin.service.AttrKeyService;
import com.wzc.lease.web.admin.service.AttrValueService;
import com.wzc.lease.web.admin.vo.attr.AttrKeyVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "房间属性管理")
@RestController
@RequestMapping("/admin/attr") // 定义当前控制器的统一请求路径前缀
public class AttrController {

    @Autowired
    private AttrKeyService attrKeyService;

    @Autowired
    private AttrValueService attrValueService;

    @Operation(summary = "新增或更新属性名称")
    @PostMapping("key/saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdateAttrKey(@RequestBody AttrKey attrKey) {
        attrKeyService.saveOrUpdate(attrKey);
        return Result.ok();
    }

    @Operation(summary = "新增或更新属性值")
    @PostMapping("value/saveOrUpdate") // 处理 HTTP POST 请求，常用于新增或更新
    public Result saveOrUpdateAttrValue(@RequestBody AttrValue attrValue) {
        attrValueService.saveOrUpdate(attrValue);
        return Result.ok();
    }

    @Operation(summary = "查询全部属性名称和属性值列表")
    @GetMapping("list") // 处理 HTTP GET 请求，常用于查询数据
    public Result<List<AttrKeyVo>> listAttrInfo() {
        List<AttrKeyVo> list = attrKeyService.listAttrInfo();
        return Result.ok();
    }

    /*
     * 【删除属性名称】
     * 为什么这个方法里面同时调用了 attrKeyService 和 attrValueService？
     * 因为"属性名"和"属性值"是一对多的关系（比如"朝向"下面有"东"、"南"、"西"多个值）。
     * 当你要删除一个属性名（如"朝向"）时，它下面挂着的那些属性值（"东"、"南"、"西"）也必须一起删掉，
     * 否则数据库里就会留下一堆"没有父亲的孤儿数据"。
     *
     * 所以这里要分两步：
     * 第一步：调用 attrKeyService.removeById() 删除 attr_key 表里的属性名本身。
     * 第二步：调用 attrValueService.remove() 删除 attr_value 表里所有属于该属性名的属性值。
     *
     * 关于 LambdaQueryWrapper 的 eq() 方法和 :: 语法：
     * - eq() 是 MyBatis-Plus 提供的条件构造方法，作用等同于 SQL 里的 WHERE xxx = yyy。
     * 比如 queryWrapper.eq(AttrValue::getAttrKeyId, attrKeyId)
     * 最终会生成 SQL：WHERE attr_key_id = #{attrKeyId}
     *
     * - :: 是 Java 8 的"方法引用"语法，AttrValue::getAttrKeyId 等价于 Lambda 表达式 (attrValue) ->
     * attrValue.getAttrKeyId()。
     * 它的作用是告诉
     * MyBatis-Plus："我要查的是 AttrValue 实体类里 getAttrKeyId 这个 getter 方法对应的数据库字段"。
     * MyBatis-Plus 内部会通过这个方法引用，自动推导出对应的数据库字段名（即 attr_key_id），
     * 这样就避免了手写字符串 "attr_key_id" 可能出现的拼写错误，更加安全可靠。
     */
    @Operation(summary = "根据id删除属性名称")
    @DeleteMapping("key/deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result removeAttrKeyById(@RequestParam Long attrKeyId) {
        // 删除属性名（操作的是 attr_key 表，所以调用 attrKeyService）
        attrKeyService.removeById(attrKeyId);
        // 删除该属性名下的所有属性值（操作的是 attr_value 表，所以调用 attrValueService）
        LambdaQueryWrapper<AttrValue> queryWrapper = new LambdaQueryWrapper<>();
        // eq(AttrValue::getAttrKeyId, attrKeyId) 等价于 SQL: WHERE attr_key_id =
        // #{attrKeyId}
        // 其中 AttrValue::getAttrKeyId 是 Java 8 方法引用，MyBatis-Plus 会自动推导出对应的数据库字段名
        // attr_key_id
        queryWrapper.eq(AttrValue::getAttrKeyId, attrKeyId);
        attrValueService.remove(queryWrapper);
        return Result.ok();
    }

    /*
     * 【删除单个属性值】
     * 这个方法只删除一条属性值（比如只删除"朝向"下面的"东"），不影响属性名本身。
     * 因为只操作 attr_value 表，所以只需要调用 attrValueService 就够了，不需要 attrKeyService。
     */
    @Operation(summary = "根据id删除属性值")
    @DeleteMapping("value/deleteById") // 处理 HTTP DELETE 请求，按ID删除数据
    public Result removeAttrValueById(@RequestParam Long id) {
        // 直接按主键 id 删除 attr_value 表中的一条记录
        attrValueService.removeById(id);
        return Result.ok();
    }

}
