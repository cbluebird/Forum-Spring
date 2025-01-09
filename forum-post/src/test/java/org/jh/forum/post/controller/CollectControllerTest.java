package org.jh.forum.post.controller;

import org.jh.forum.post.dto.CollectDTO;
import org.jh.forum.post.service.ICollectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CollectControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ICollectService collectService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCollectPost() throws Exception {
        CollectDTO collectDTO = new CollectDTO();
        collectDTO.setId(1);

        mockMvc.perform(post("/api/post/collect")
                        .header("X-User-ID", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"postId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUncollectPost() throws Exception {
        CollectDTO collectDTO = new CollectDTO();
        collectDTO.setId(1);

        mockMvc.perform(post("/api/post/uncollect")
                        .header("X-User-ID", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"postId\":1}"))
                .andExpect(status().isOk());
    }
}