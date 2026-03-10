package com.wzc.lease.web.admin.service;

import com.wzc.lease.web.admin.vo.login.CaptchaVo;
import com.wzc.lease.web.admin.vo.login.LoginVo;
import com.wzc.lease.web.admin.vo.system.user.SystemUserInfoVo;

public interface LoginService {

    CaptchaVo getCaptcha();

    String login(LoginVo loginVo);

    SystemUserInfoVo getLoginUserInfoById(Long userId);
}
