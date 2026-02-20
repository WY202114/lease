package com.wzc.lease.web.admin.custom.config; // 管理端文档配置所在包

import io.swagger.v3.oas.models.OpenAPI; // OpenAPI 顶层文档对象
import io.swagger.v3.oas.models.info.Info; // 文档基础信息对象
import org.springdoc.core.models.GroupedOpenApi; // 按路径分组的文档配置对象
import org.springframework.context.annotation.Bean; // 将方法返回值注册为 Bean
import org.springframework.context.annotation.Configuration; // 声明配置类

/**
 * @author 武振川
 * @version 1.0
 */
@Configuration // 使该类在 Spring 启动时作为配置生效
public class Knife4jConfiguration { // Knife4j / SpringDoc 文档配置类

    @Bean // 注册全局 OpenAPI 文档信息
    public OpenAPI customOpenAPI() { // 配置文档标题、版本、描述

        return new OpenAPI().info( // 创建 OpenAPI 并设置基础信息
                new Info() // 创建文档信息对象
                        .title("后台管理系统API") // 文档标题
                        .version("1.0") // 文档版本
                        .description("后台管理系统API")); // 文档描述
    }

    @Bean // 注册系统模块接口分组
    public GroupedOpenApi systemAPI() { // 系统相关接口分组配置

        return GroupedOpenApi.builder().group("系统信息管理"). // 分组名称
                pathsToMatch( // 该分组匹配的接口路径
                        "/admin/system/**" // 系统管理接口
                ).
                build(); // 构建分组对象
    }

    @Bean // 注册登录模块接口分组
    public GroupedOpenApi loginAPI() { // 登录相关接口分组配置

        return GroupedOpenApi.builder().group("后台登录管理"). // 分组名称
                pathsToMatch( // 该分组匹配的接口路径
                        "/admin/login/**", // 登录接口
                        "/admin/info" // 当前用户信息接口
                ).
                build(); // 构建分组对象
    }

    @Bean // 注册公寓模块接口分组
    public GroupedOpenApi apartmentAPI() { // 公寓相关接口分组配置

        return GroupedOpenApi.builder().group("公寓信息管理"). // 分组名称
                pathsToMatch( // 该分组匹配的接口路径
                        "/admin/apartment/**", // 公寓管理接口
                        "/admin/room/**", // 房间管理接口
                        "/admin/label/**", // 标签管理接口
                        "/admin/facility/**", // 配套设施接口
                        "/admin/fee/**", // 费用配置接口
                        "/admin/attr/**", // 属性管理接口
                        "/admin/payment/**", // 支付方式接口
                        "/admin/region/**", // 区域管理接口
                        "/admin/term/**", // 租期管理接口
                        "/admin/file/**" // 文件管理接口
                ).build(); // 构建分组对象
    }

    @Bean // 注册租赁模块接口分组
    public GroupedOpenApi leaseAPI() { // 租赁相关接口分组配置
        return GroupedOpenApi.builder().group("租赁信息管理"). // 分组名称
                pathsToMatch( // 该分组匹配的接口路径
                        "/admin/appointment/**", // 预约管理接口
                        "/admin/agreement/**" // 协议/合同管理接口
                ).build(); // 构建分组对象
    }

    @Bean // 注册平台用户模块接口分组
    public GroupedOpenApi userAPI() { // 用户相关接口分组配置
        return GroupedOpenApi.builder().group("平台用户管理"). // 分组名称
                pathsToMatch( // 该分组匹配的接口路径
                        "/admin/user/**" // 用户管理接口
                ).build(); // 构建分组对象
    }
}
