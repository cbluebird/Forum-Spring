package org.jh.forum.post.controller;

import org.jh.forum.post.dto.PostDTO;
import org.jh.forum.post.service.IPostService;
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
public class PostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IPostService postService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAddPost() throws Exception {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Test Post");
        postDTO.setContent("This is a test post.");

        mockMvc.perform(post("/api/post")
                        .header("X-User-ID", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Test Post\",\"content\":\"This is a test post.\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePost() throws Exception {
        mockMvc.perform(delete("/api/post/1")
                        .header("X-User-ID", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPost() throws Exception {
        mockMvc.perform(get("/api/post/1")
                        .header("X-User-ID", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"));
    }

    @Test
    public void testGetPostListByCategory() throws Exception {
        mockMvc.perform(get("/api/post/list/category/1")
                        .header("X-User-ID", "1")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPostListByTag() throws Exception {
        mockMvc.perform(get("/api/post/list/tag/1")
                        .header("X-User-ID", "1")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPostListByUser() throws Exception {
        mockMvc.perform(get("/api/post/list/user/1")
                        .header("X-User-ID", "1")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUpvotePostList() throws Exception {
        mockMvc.perform(get("/api/post/upvote/list")
                        .header("X-User-ID", "1")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCollectPostList() throws Exception {
        mockMvc.perform(get("/api/post/collect/list")
                        .header("X-User-ID", "1")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetHotPostOfDay() throws Exception {
        mockMvc.perform(get("/api/post/hot/day")
                        .header("X-User-ID", "1")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }
}