package org.jh.forum.user.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.constant.FeignConstant;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.user.constant.UserStatus;
import org.jh.forum.user.constant.UserType;
import org.jh.forum.user.dto.UserBatchDTO;
import org.jh.forum.user.dto.UserDTO;
import org.jh.forum.user.model.User;
import org.jh.forum.user.service.IUserService;
import org.jh.forum.user.vo.UserVO;
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
    public void add(@RequestBody @Validated(UserDTO.Register.class) UserDTO userDTO) {
        if (userService.getOne(new QueryWrapper<User>().eq("username", userDTO.getUsername())) != null) {
            throw new BizException(ErrorCode.USERNAME_ALREADY_EXISTS, "用户名: " + userDTO.getUsername() + " 已存在");
        }
        if (userService.getOne(new QueryWrapper<User>().eq("phone", userDTO.getPhone())) != null) {
            throw new BizException(ErrorCode.PHONE_ALREADY_BIND, "手机号: " + userDTO.getPhone() + " 已绑定");
        }
        if (userService.getOne(new QueryWrapper<User>().eq("email", userDTO.getEmail())) != null) {
            throw new BizException(ErrorCode.EMAIL_ALREADY_BIND, "邮箱: " + userDTO.getEmail() + " 已绑定");
        }
        User user = new User();
        BeanUtil.copyProperties(userDTO, user, "password");
        user.setPassword(BCrypt.hashpw(userDTO.getPassword()));
        user.setType(UserType.ORDINARY);
        user.setStatus(UserStatus.ACTIVE);
        userService.save(user);
    }

    @PutMapping("/info")
    public void update(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated(UserDTO.Update.class) UserDTO userDTO) {
        User user = userService.getById(userId);
        if (!user.getUsername().equals(userDTO.getUsername()) && userService.getOne(new QueryWrapper<User>().eq("username", userDTO.getUsername())) != null) {
            throw new BizException(ErrorCode.USERNAME_ALREADY_EXISTS, "用户名: " + userDTO.getUsername() + " 已存在");
        }
        if (!user.getPhone().equals(userDTO.getPhone()) && userService.getOne(new QueryWrapper<User>().eq("phone", userDTO.getPhone())) != null) {
            throw new BizException(ErrorCode.PHONE_ALREADY_BIND, "手机号: " + userDTO.getPhone() + " 已绑定");
        }
        if (!user.getEmail().equals(userDTO.getEmail()) && userService.getOne(new QueryWrapper<User>().eq("email", userDTO.getEmail())) != null) {
            throw new BizException(ErrorCode.EMAIL_ALREADY_BIND, "邮箱: " + userDTO.getEmail() + " 已绑定");
        }
        BeanUtil.copyProperties(userDTO, user, "id", "password", "type", "status");
        userService.updateById(user);
    }

    @GetMapping("/info")
    public UserVO getUserInfo(@RequestHeader("X-User-ID") String userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND);
        }
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        return userVO;
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@RequestHeader(name = FeignConstant.F_REQUEST_ID, required = false) String feignRequestId, @PathVariable("username") @NotBlank(message = "用户名不能为空") String username) {
        if (feignRequestId == null) {
            throw new BizException(ErrorCode.COMMON_DISABLE_EXTERNAL_CALL);
        }
        return userService.getOne(new QueryWrapper<User>().eq("username", username));
    }

    @GetMapping("/id/{userId}")
    public User getUserById(@PathVariable("userId") @NotNull(message = "User ID 不能为空") Integer userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    @PostMapping("/batch-get")
    public List<User> getUserByIds(@RequestBody @Validated UserBatchDTO userBatchDTO) {
        List<Integer> userIds = userBatchDTO.getUserIds();
        List<User> users = userService.listByIds(userIds);
        if (users.size() != userIds.size()) {
            List<Integer> foundUserIds = users.stream().map(User::getId).toList();
            List<Integer> missingUserIds = userIds.stream().filter(id -> !foundUserIds.contains(id)).toList();
            throw new BizException(ErrorCode.USER_NOT_FOUND, "Missing user IDs: " + missingUserIds);
        }
        return users;
    }
}
