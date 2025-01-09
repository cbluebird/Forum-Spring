package org.jh.forum.auth.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import org.jh.forum.auth.dto.UserDTO;
import org.jh.forum.auth.dto.UserFeignDTO;
import org.jh.forum.auth.feign.UserFeign;
import org.jh.forum.auth.service.impl.AuthServiceImpl;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.exception.BizException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserFeign userFeign;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");

        UserFeignDTO userFeignDTO = new UserFeignDTO();
        userFeignDTO.setId(1);
        userFeignDTO.setUsername("testuser");
        userFeignDTO.setPassword(BCrypt.hashpw("password"));

        when(userFeign.getUserByUsername("testuser")).thenReturn(userFeignDTO);

        authService.login(userDTO);

        verify(userFeign, times(1)).getUserByUsername("testuser");
        assertTrue(StpUtil.isLogin());
    }

    @Test
    public void testLoginUserNotFound() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");

        when(userFeign.getUserByUsername("testuser")).thenReturn(null);

        BizException exception = assertThrows(BizException.class, () -> authService.login(userDTO));

        assertEquals(ErrorCode.AUTH_USERNAME_OR_PASSWORD_ERROR, exception.getErrorCode());
        assertEquals("登录用户名: testuser 不存在", exception.getMessage());
    }

    @Test
    public void testLoginPasswordError() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("wrongpassword");

        UserFeignDTO userFeignDTO = new UserFeignDTO();
        userFeignDTO.setId(1);
        userFeignDTO.setUsername("testuser");
        userFeignDTO.setPassword(BCrypt.hashpw("password"));

        when(userFeign.getUserByUsername("testuser")).thenReturn(userFeignDTO);

        BizException exception = assertThrows(BizException.class, () -> authService.login(userDTO));

        assertEquals(ErrorCode.AUTH_USERNAME_OR_PASSWORD_ERROR, exception.getErrorCode());
        assertEquals("登录用户名: testuser 密码错误", exception.getMessage());
    }
}