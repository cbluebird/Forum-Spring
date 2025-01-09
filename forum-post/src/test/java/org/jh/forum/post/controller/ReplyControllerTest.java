package org.jh.forum.post.controller;

import org.jh.forum.post.dto.ReplyDTO;
import org.jh.forum.post.service.IReplyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ReplyControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IReplyService replyService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAddReply() throws Exception {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setPostId(1);
        replyDTO.setContent("This is a test reply.");

        mockMvc.perform(post("/api/post/reply")
                        .header("X-User-ID", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"postId\":1,\"content\":\"This is a test reply.\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteReply() throws Exception {
        mockMvc.perform(delete("/api/post/reply/1")
                        .header("X-User-ID", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetReplyList() throws Exception {
        mockMvc.perform(get("/api/post/reply/list")
                        .header("X-User-ID", "1")
                        .param("postId", "1"))
                .andExpect(status().isOk());
    }
}