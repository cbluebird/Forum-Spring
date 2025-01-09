package org.jh.forum.post.controller;

import org.jh.forum.post.dto.TagDTO;
import org.jh.forum.post.service.ITagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TagControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ITagService tagService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAddTag() throws Exception {
        TagDTO tagDTO = new TagDTO();
        tagDTO.setName("Test Tag");

        mockMvc.perform(post("/api/post/tag")
                        .header("X-User-ID", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Tag\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetTag() throws Exception {
        mockMvc.perform(get("/api/post/tag/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Tag"));
    }

    @Test
    public void testGetHotTagOfDay() throws Exception {
        mockMvc.perform(get("/api/post/tag/hot/day")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());
    }
}