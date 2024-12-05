package org.jh.forum.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.BCrypt;
import org.jh.forum.auth.dto.UserDTO;
import org.jh.forum.auth.feign.UserFeign;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserFeign userFeign;

    @PostMapping("/login")
    public void login(@RequestBody @Validated UserDTO userDTO) {
        Object user = userFeign.getUserByUsername(userDTO.getUsername());
        Map<String, Object> userMap = BeanUtil.beanToMap(user);
        if (!BCrypt.checkpw(userDTO.getPassword(), (String) userMap.get("password"))) {
            throw new BizException(ErrorCode.AUTH_PASSWORD_ERROR);
        }
        StpUtil.login(userMap.get("id"));
    }
}
