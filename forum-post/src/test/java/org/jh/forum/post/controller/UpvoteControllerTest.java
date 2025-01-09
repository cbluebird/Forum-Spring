package org.jh.forum.post.controller;

import org.jh.forum.post.dto.UpvoteDTO;
import org.jh.forum.post.service.IUpvoteService;
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
public class UpvoteControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IUpvoteService upvoteService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testUpvotePost() throws Exception {
        UpvoteDTO upvoteDTO = new UpvoteDTO();
        upvoteDTO.setId(1);

        mockMvc.perform(post("/api/post/upvote")
                        .header("X-User-ID", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"postId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDownvotePost() throws Exception {
        UpvoteDTO upvoteDTO = new UpvoteDTO();
        upvoteDTO.setId(1);

        mockMvc.perform(post("/api/post/downvote")
                        .header("X-User-ID", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"postId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpvoteReply() throws Exception {
        UpvoteDTO upvoteDTO = new UpvoteDTO();
        upvoteDTO.setId(1);

        mockMvc.perform(post("/api/post/reply/upvote")
                        .header("X-User-ID", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"replyId\":1}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDownvoteReply() throws Exception {
        UpvoteDTO upvoteDTO = new UpvoteDTO();
        upvoteDTO.setId(1);

        mockMvc.perform(post("/api/post/reply/downvote")
                        .header("X-User-ID", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"replyId\":1}"))
                .andExpect(status().isOk());
    }
}