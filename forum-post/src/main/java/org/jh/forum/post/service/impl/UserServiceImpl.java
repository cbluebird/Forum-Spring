package org.jh.forum.post.service.impl;

import cn.hutool.core.bean.BeanUtil;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.post.dto.UserDTO;
import org.jh.forum.post.feign.UserFeign;
import org.jh.forum.post.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserFeign userFeign;

    @Override
    public UserDTO ensureUserExist(String id) {
        UserDTO userDTO = userFeign.getUserById(Integer.valueOf(id));
        if (userDTO == null) {
            throw new BizException(ErrorCode.USER_NOT_FOUND, "用户ID: " + id + " 不存在");
        }
        return userDTO;
    }

    @Override
    public void checkAdminPermission(String id) {
        Object user = userFeign.getUserById(Integer.valueOf(id));
        Map<String, Object> userMap = BeanUtil.beanToMap(user);
        if ((Integer) userMap.get("type") != 2) {
            throw new BizException(ErrorCode.COMMON_PERMISSION_ERROR, "用户ID: " + id + " 没有权限");
        }
    }
}