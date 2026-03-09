package com.wzc.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzc.lease.model.entity.SystemPost;
import com.wzc.lease.model.entity.SystemUser;
import com.wzc.lease.web.admin.mapper.SystemPostMapper;
import com.wzc.lease.web.admin.mapper.SystemUserMapper;
import com.wzc.lease.web.admin.service.SystemUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzc.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.wzc.lease.web.admin.vo.system.user.SystemUserQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liubo
 * @description 针对表【system_user(员工信息表)】的数据库操作Service实现
 * @createDate 2023-07-24 15:48:00
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser>
        implements SystemUserService {

    @Autowired
    private SystemUserMapper systemUserMapper;

    @Autowired
    private SystemPostMapper systemPostMapper;

    @Override
    public IPage<SystemUserItemVo> pageSystemUser(Page<SystemUser> page, SystemUserQueryVo queryVo) {
        return systemUserMapper.pageSystemUser(page, queryVo);
    }

    /**
     * 根据用户 ID 查询用户详细信息（包含岗位名称）
     *
     * 执行流程：
     * 1. 先根据 id 查 system_user 表，拿到用户基本信息（包括 post_id）
     * 2. 再用用户的 post_id 去查 system_post 表，拿到岗位名称
     * 3. 创建 VO 对象，将用户属性拷贝进去，再手动设置岗位名称
     * 4. 返回组装好的 VO
     */
    @Override
    public SystemUserItemVo getSystemUserById(Long id) {
        // 【第一步 - 必须先执行】根据用户 id 查询 system_user 表，获取用户基本信息
        // 答：必须先查这个，因为下一步要用到 systemUser.getPostId()，
        // 如果不先查用户，就拿不到 post_id，也就无法知道该去查哪个岗位。
        SystemUser systemUser = systemUserMapper.selectById(id);

        // 【第二步 - 依赖第一步的结果】根据用户的 postId 查询 system_post 表，获取岗位信息
        // 答：这一步依赖于第一步查出的 systemUser.getPostId()，所以必须写在后面。
        // 这就是"数据依赖关系"：B 的输入来自 A 的输出，所以 A 必须在 B 之前执行。
        SystemPost systemPost = systemPostMapper.selectById(systemUser.getPostId());

        // 【第三步】创建返回给前端的 VO（View Object）对象
        SystemUserItemVo systemUserItemVo = new SystemUserItemVo();

        // 【第四步】使用 Spring 的 BeanUtils 工具，将 systemUser 中同名同类型的属性值
        // 自动拷贝到 systemUserItemVo 中（如 id, username, name, phone 等）
        // 避免了手动逐个 set 的繁琐写法
        BeanUtils.copyProperties(systemUser, systemUserItemVo);

        // 【第五步】手动设置岗位名称（因为 postName 不在 systemUser 中，无法自动拷贝）
        systemUserItemVo.setPostName(systemPost.getName());

        return systemUserItemVo;
    }
}
