package org.jh.forum.user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.constraints.NotBlank;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.user.constant.UserStatus;
import org.jh.forum.user.constant.UserType;
import org.jh.forum.user.dto.UserDTO;
import org.jh.forum.user.model.User;
import org.jh.forum.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public void add(@RequestBody @Validated UserDTO userDTO) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", userDTO.getUsername()));
        if (user != null) {
            throw new BizException(ErrorCode.USER_ALREADY_EXISTS);
        }
        user = new User();
        BeanUtil.copyProperties(userDTO, user, "password");
        user.setPassword(BCrypt.hashpw(userDTO.getPassword()));
        user.setType(UserType.ORDINARY);
        user.setStatus(UserStatus.ACTIVE);
        userService.save(user);
    }

    @GetMapping
    public User getUserByUsername(@RequestParam("username") @NotBlank(message = "用户名不能为空") String username) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }
}
