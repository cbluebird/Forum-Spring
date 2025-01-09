package org.jh.forum.post.controller;

import org.jh.forum.post.dto.CategoryDTO;
import org.jh.forum.post.service.ICategoryService;
import org.jh.forum.post.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
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
public class CategoryControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IUserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAddCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Test Category");

        mockMvc.perform(post("/api/post/category")
                        .header("X-User-ID", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Category\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/api/post/category/1")
                        .header("X-User-ID", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Updated Category");

        mockMvc.perform(put("/api/post/category/1")
                        .header("X-User-ID", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Category\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCategory() throws Exception {
        mockMvc.perform(get("/api/post/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    public void testGetCategoryList() throws Exception {
        mockMvc.perform(get("/api/post/category/list")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }
}