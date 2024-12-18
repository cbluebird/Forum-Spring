package org.jh.forum.user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.user.constant.UserStatus;
import org.jh.forum.user.constant.UserType;
import org.jh.forum.user.dto.UserBatchDTO;
import org.jh.forum.user.dto.UserDTO;
import org.jh.forum.user.model.User;
import org.jh.forum.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable("username") @NotBlank(message = "用户名不能为空") String username) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    @GetMapping("/id/{userId}")
    public User getUserById(@PathVariable("userId") @NotNull(message = "User ID 不能为空") Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    @PostMapping("/batch-get")
    public List<User> getUserByIds(@RequestBody @Validated UserBatchDTO userBatchDTO) {
        List<Long> userIds = userBatchDTO.getUserIds();
        List<User> users = userService.listByIds(userIds);
        if (users.size() != userIds.size()) {
            List<Long> foundUserIds = users.stream().map(User::getId).toList();
            List<Long> missingUserIds = userIds.stream().filter(id -> !foundUserIds.contains(id)).toList();
            throw new BizException(ErrorCode.USER_NOT_FOUND, "Missing user IDs: " + missingUserIds);
        }
        return users;
    }
}
