package com.wzc.lease.web.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wf.captcha.SpecCaptcha;
import com.wzc.lease.common.constant.RedisConstant;
import com.wzc.lease.common.exception.LeaseException;
import com.wzc.lease.common.result.ResultCodeEnum;
import com.wzc.lease.common.utils.JwtUtil;
import com.wzc.lease.model.entity.SystemUser;
import com.wzc.lease.model.enums.BaseStatus;
import com.wzc.lease.web.admin.mapper.SystemUserMapper;
import com.wzc.lease.web.admin.service.LoginService;
import com.wzc.lease.web.admin.vo.login.CaptchaVo;
import com.wzc.lease.web.admin.vo.login.LoginVo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SystemUserMapper systemUserMapper;// 为什么要注入它？

    /**
     * 获取图形验证码并缓存
     *
     * 业务场景：用户在登录页面点击获取验证码。
     * 逻辑流程：
     * 1. 【生成内容】：生成一张带有随机字符的图片（防止机器暴力破解）。
     * 2. 【建立关联】：生成一个全局唯一的凭证（Key），并将验证码文本存入Redis缓存中（带过期时间）。
     * 3. 【返回结果】：将图片数据和凭证返回给前端，前端在登录时需携带此凭证和用户输入的字符进行校验。
     *
     * @return 包含图片Base64编码和Redis键的视图对象
     */
    @Override
    public CaptchaVo getCaptcha() {
        // 第一步：【生成内容】利用第三方工具生成图形验证码（宽130, 高48, 长度4位）
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);

        // 提取验证码上的文字内容，并全部转为小写。
        // 目的：为了在后续登录比对时，能够实现“忽略大小写”的用户友好体验。
        String code = specCaptcha.text().toLowerCase();

        // 第二步：【建立关联】生成一个唯一的标识符（UUID），作为存入Redis的Key。
        // 目的：因为HTTP是无状态的，需要通过这个Key将“当前生成的验证码”与“未来的某次登录请求”绑定起来。
        // String key = "admin:login:" + UUID.randomUUID();
        String key = RedisConstant.ADMIN_LOGIN_PREFIX + UUID.randomUUID();

        // 将验证码文本存入Redis缓存。
        // 目的：暂存正确的验证码答案。设置60秒有效期，既能保证安全性（防重放），又能及时释放内存。
        // stringRedisTemplate.opsForValue().set(key, code, 60, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(key, code, RedisConstant.ADMIN_LOGIN_CAPTCHA_TTL_SEC, TimeUnit.SECONDS);

        // 第三步：【返回结果】组装并返回给前端。
        // 目的：前端拿到 Base64 字符串可以直接渲染为图片展示给用户，拿到 key 可以在用户提交登录表单时一并带回到后端。
        return new CaptchaVo(specCaptcha.toBase64(), key);
    }

    /**
     * 管理后台用户登录逻辑
     * 前端会发送 username、password、captchaKey、captchaCode 请求登录。
     *
     * @param loginVo 包含用户登录信息的实体
     * @return 登录成功后返回的Token字符串（目前占位返回空字符串）
     */
    @Override
    public String login(LoginVo loginVo) {
        // 1. 判断前端传来的 captchaCode 是否为空
        // 若为空，说明用户没有填写验证码，直接响应“验证码为空”
        if (loginVo.getCaptchaCode() == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_NOT_FOUND);
        }

        // 2. 根据 captchaKey 从 Redis 中查询之前保存的真实验证码 code
        String code = stringRedisTemplate.opsForValue().get(loginVo.getCaptchaKey());

        // 若查询出来的 code 为空，说明验证码已经过了 60 秒有效期被 Redis 删除了，直接响应“验证码已过期”
        if (code == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_EXPIRED);
        }

        // 3. 比较前端传来的 captchaCode（统一转小写）和从 Redis 取出的真实 code
        // 若不相同，直接响应“验证码不正确”
        if (!code.equals(loginVo.getCaptchaCode().toLowerCase())) {
            throw new LeaseException(ResultCodeEnum.ADMIN_CAPTCHA_CODE_ERROR);
        }

        // 4. 验证码校验通过后，根据 username 查询数据库
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUsername, loginVo.getUsername());
        SystemUser systemUser = systemUserMapper.selectOne(queryWrapper);

        // 为什么判空？
        // 因为用户输入的账号可能根本不在数据库中（比如输错了拼音）。
        // selectOne 查不到记录时会返回 null，如果这里不判空，下面调用 systemUser.getStatus() 就会抛出
        // NullPointerException。
        // 所以若查询结果为空，直接响应“账号不存在”
        if (systemUser == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_NOT_EXIST_ERROR);
        }

        // 5. 查看用户状态，判断该用户是否被管理员拉黑或禁用
        // 若状态为 DISABLE（禁用），直接响应“账号被禁”
        if (systemUser.getStatus() == BaseStatus.DISABLE) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_DISABLED_ERROR);
        }

        // 6. 比对 password 和数据库中查询的密码
        // DigestUtils.md5Hex 干什么用的？
        // 答：它是用来对字符串进行 MD5 加密的。出于安全考虑，数据库中绝对不能明文存储用户密码（比如不能直接存"123456"）。
        // 因此在注册/添加用户时，系统会将"123456"经 MD5 加密成如"e10adc3949ba59abbe56e057f20f883e"的长串存入数据库。
        // 现在登录时，必须将用户刚输入的明文密码，用同样的 MD5 算法加一次密，再和数据库里那串密文去比对。
        // 注意：这里逻辑上应该是“如果不一致，则报错”，所以用 !equals。
        if (!systemUser.getPassword().equals(DigestUtils.md5Hex(loginVo.getPassword()))) {
            throw new LeaseException(ResultCodeEnum.ADMIN_ACCOUNT_ERROR);
        }

        // 7. 所有校验全部通过，进入最后一步：签发 Token（这里暂时代替返回空字符串）
        return JwtUtil.createToken(systemUser.getId(), systemUser.getUsername());
    }
}
