package org.jh.forum.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jh.forum.user.model.User;
import org.jh.forum.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setPhone("1234567890");
        user.setEmail("test@example.com");
        user.setPassword("password");

        boolean isSaved = userService.save(user);
        assertTrue(isSaved);
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setUsername("testUser");
        user.setPhone("1234567890");
        user.setEmail("test@example.com");
        user.setPassword("password");

        userService.save(user);
        User foundUser = userService.getById(user.getId());
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    public void testGetUserByUsername() {
        User user = new User();
        user.setUsername("testUser");
        user.setPhone("1234567890");
        user.setEmail("test@example.com");
        user.setPassword("password");

        userService.save(user);
        User foundUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        assertNotNull(foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    public void testListUsers() {
        List<User> users = userService.list();
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    public void testDeleteUserById() {
        User user = new User();
        user.setUsername("testUser");
        user.setPhone("1234567890");
        user.setEmail("test@example.com");
        user.setPassword("password");

        userService.save(user);
        boolean isDeleted = userService.removeById(user.getId());
        assertTrue(isDeleted);

        User foundUser = userService.getById(user.getId());
        assertNull(foundUser);
    }
}