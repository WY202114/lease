package com.wzc.lease.web.admin.mapper;

import com.wzc.lease.model.entity.GraphInfo;
import com.wzc.lease.model.enums.ItemType;
import com.wzc.lease.web.admin.vo.graph.GraphVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author liubo
 * @description 针对表【graph_info(图片信息表)】的数据库操作Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.wzc.lease.model.GraphInfo
 */
/*
 * 【MyBatis 在项目启动时做的事情】
 *
 * 1. 扫描所有 Mapper XML 文件
 * 2. 读取每个 XML 的 namespace 和 <select> 的 id，建立一张"映射表"：
 * namespace = com.wzc.lease.web.admin.mapper.GraphInfoMapper（对应这个接口的全限定类名）
 * id = selectByItemTypeAndId（对应这个接口中的方法名）
 * 3. 当 Service 调用 graphInfoMapper.selectByItemTypeAndId(...) 时，MyBatis 拿着：
 * - 接口全名（com.wzc.lease.web.admin.mapper.GraphInfoMapper）→ 匹配 XML 的 namespace
 * - 方法名（selectByItemTypeAndId）→ 匹配 XML 中 <select> 的 id
 * 找到对应的 SQL → 填入参数 → 交给数据库执行 → 将结果映射成 Java 对象返回
 *
 * 简单说：接口本身不干活，它就像"前台接待员"，
 * 根据"通讯录"（namespace + id 的映射表）找到 XML 中的 SQL 去执行
 */
public interface GraphInfoMapper extends BaseMapper<GraphInfo> {

    // 这个方法没有实现代码，MyBatis 通过方法名 selectByItemTypeAndId
    // 匹配 GraphInfoMapper.xml 中 <select id="selectByItemTypeAndId"> 的 SQL 来执行
    List<GraphVo> selectByItemTypeAndId(ItemType itemType, Long id);
    /*
     * 【SQL 执行后，结果是怎么一步步返回给 graphVoList 的？】
     *
     * 1. 数据库执行 SQL，返回原始行数据（像 Excel 表格）：
     * 行1: name=客厅图, url=http://xxx/1.jpg
     * 行2: name=卧室图, url=http://xxx/2.jpg
     *
     * 2. MyBatis 看 XML 中的 resultType="GraphVo"，知道每行要变成 GraphVo 对象，
     * 于是自动对每一行执行：
     * GraphVo obj1 = new GraphVo(); ← 新建空对象（餐盒）
     * obj1.setName("客厅图"); ← 列名 name 匹配属性 name，赋值（装菜）
     * obj1.setUrl("http://xxx/1.jpg"); ← 列名 url 匹配属性 url，赋值
     * （第 2 行同理...）
     *
     * 3. 把所有对象装进一个 ArrayList（外卖袋）：
     * List<GraphVo> result = [obj1, obj2];
     *
     * 4. 代理对象把这个 List return 回去，你的变量接住：
     * List<GraphVo> graphVoList = 上面那个 result;
     *
     * 总结：数据库行数据 → new 对象 + set 属性 → 放入 List → 返回给你的变量
     */
}
