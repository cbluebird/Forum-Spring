package org.jh.forum.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import org.jh.forum.auth.dto.UserDTO;
import org.jh.forum.auth.dto.UserFeignDTO;
import org.jh.forum.auth.feign.UserFeign;
import org.jh.forum.auth.service.IAuthService;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {
    @Autowired
    private UserFeign userFeign;

    @Override
    public void login(UserDTO userDTO) {
        String username = userDTO.getUsername();
        UserFeignDTO userFeignDTO = userFeign.getUserByUsername(username);
        if (userFeignDTO == null) {
            throw new BizException(ErrorCode.AUTH_USERNAME_OR_PASSWORD_ERROR, "登录用户名: " + username + " 不存在");
        }
        if (!BCrypt.checkpw(userDTO.getPassword(), userFeignDTO.getPassword())) {
            throw new BizException(ErrorCode.AUTH_USERNAME_OR_PASSWORD_ERROR, "登录用户名: " + username + " 密码错误");
        }
        StpUtil.login(userFeignDTO.getId());
    }
}