package com.wzc.lease.web.admin.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.lease.model.entity.LeaseAgreement;
import com.wzc.lease.web.admin.vo.agreement.AgreementQueryVo;
import com.wzc.lease.web.admin.vo.agreement.AgreementVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 租约信息表的数据库操作 Mapper 接口
 *
 * <p>
 * 【MyBatis 如何从这个接口方法找到 XML 中的 SQL？】
 *
 * 靠两把"钥匙"匹配：
 *
 * 🔑 第一把钥匙：namespace（命名空间）
 * XML 文件中的 namespace 必须 = 本接口的全限定类名：
 * {@code <mapper namespace=
 * "com.wzc.lease.web.admin.mapper.LeaseAgreementMapper">}
 * → MyBatis 就知道这个 XML 是给 LeaseAgreementMapper 接口用的
 *
 * 🔑 第二把钥匙：id（方法名）
 * XML 中 {@code <select id="pageAgreement">} 的 id 值必须 = 接口中的方法名 pageAgreement
 * → 方法名和 id 完全一致，就对上了
 *
 * 完整查找过程：
 * 1. 调用 mapper.pageAgreement(page, queryVo)
 * 2. MyBatis 拿着接口全限定类名找到对应的 XML 文件（通过 namespace 匹配）
 * 3. 再拿着方法名找到具体的 SQL 语句（通过 id 匹配）
 * 4. 把方法参数传入 XML 中的 #{} 占位符（如 #{queryVo.provinceId}）
 * 5. 执行 SQL，把结果按 resultMap 映射成 AgreementVo 对象返回
 *
 * MyBatis 怎么知道 XML 文件在哪？
 * 通过 application.yml 中的配置：
 * mybatis-plus.mapper-locations=classpath:mapper/*.xml
 * → 启动时扫描 resources/mapper/ 下所有 XML，注册 namespace + id 到全局映射表
 * </p>
 *
 * @author liubo
 * @description 针对表【lease_agreement(租约信息表)】的数据库操作Mapper
 * @createDate 2023-07-24 15:48:00
 * @Entity com.wzc.lease.model.LeaseAgreement
 */
public interface LeaseAgreementMapper extends BaseMapper<LeaseAgreement> {

    /**
     * 分页查询租约列表（自定义多表联查）
     *
     * 该方法名 "pageAgreement" 会匹配到 XML 中 id="pageAgreement" 的 SQL：
     * 对应文件：resources/mapper/LeaseAgreementMapper.xml
     *
     * @param page    分页对象（current=页码, size=每页条数），MyBatis-Plus 分页插件会自动拼接 LIMIT
     * @param queryVo 查询条件对象，字段不为 null 的会被 XML 中的 {@code <if>} 标签拼入 WHERE 子句
     * @return 分页结果，包含 records（数据列表）、total（总记录数）、pages（总页数）等
     */
    IPage<AgreementVo> pageAgreement(Page<AgreementVo> page, AgreementQueryVo queryVo);
}
