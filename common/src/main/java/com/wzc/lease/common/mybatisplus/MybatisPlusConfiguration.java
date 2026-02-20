package com.wzc.lease.common.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author 武振川
 */
@Configuration // 标记为Spring配置类，让该类中的配置可被容器加载
@MapperScan("com.wzc.lease.web.*.mapper") // 扫描Mapper接口并注册为Spring Bean，避免逐个添加@Mapper
public class MybatisPlusConfiguration {

}


