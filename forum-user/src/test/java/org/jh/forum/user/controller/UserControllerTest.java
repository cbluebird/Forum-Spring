package org.jh.forum.user.controller;

import org.jh.forum.user.dto.UserDTO;
import org.jh.forum.user.model.User;
import org.jh.forum.user.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IUserService userService;

    private MockMvc mockMvc;

    @Test
    public void testRegisterUser() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPhone("1234567890");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser\",\"phone\":\"1234567890\",\"email\":\"test@example.com\",\"password\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateUserInfo() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        User user = new User();
        user.setUsername("testUser");
        user.setPhone("1234567890");
        user.setEmail("test@example.com");
        user.setPassword("password");
        userService.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updatedUser");
        userDTO.setPhone("0987654321");
        userDTO.setEmail("updated@example.com");

        mockMvc.perform(put("/api/user/info")
                        .header("X-User-ID", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updatedUser\",\"phone\":\"0987654321\",\"email\":\"updated@example.com\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUserInfo() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        User user = new User();
        user.setUsername("testUser");
        user.setPhone("1234567890");
        user.setEmail("test@example.com");
        user.setPassword("password");
        userService.save(user);

        mockMvc.perform(get("/api/user/info")
                        .header("X-User-ID", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    public void testGetUserByUsername() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        User user = new User();
        user.setUsername("testUser");
        user.setPhone("1234567890");
        user.setEmail("test@example.com");
        user.setPassword("password");
        userService.save(user);

        mockMvc.perform(get("/api/user/username/testUser")
                        .header("X-Request-ID", "testRequestId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    public void testGetUserById() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        User user = new User();
        user.setUsername("testUser");
        user.setPhone("1234567890");
        user.setEmail("test@example.com");
        user.setPassword("password");
        userService.save(user);

        mockMvc.perform(get("/api/user/id/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    public void testBatchGetUsers() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        User user1 = new User();
        user1.setUsername("testUser1");
        user1.setPhone("1234567890");
        user1.setEmail("test1@example.com");
        user1.setPassword("password");
        userService.save(user1);

        User user2 = new User();
        user2.setUsername("testUser2");
        user2.setPhone("0987654321");
        user2.setEmail("test2@example.com");
        user2.setPassword("password");
        userService.save(user2);

        mockMvc.perform(post("/api/user/batch-get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"" + user1.getId() + "\", \"" + user2.getId() + "\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testUser1"))
                .andExpect(jsonPath("$[1].username").value("testUser2"));
    }
}