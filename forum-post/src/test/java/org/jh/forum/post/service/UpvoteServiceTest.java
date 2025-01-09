package org.jh.forum.post.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jh.forum.post.dto.UpvoteDTO;
import org.jh.forum.post.model.Upvote;
import org.jh.forum.post.service.impl.UpvoteServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UpvoteServiceTest {

    @Autowired
    private UpvoteServiceImpl upvoteService;

    @Test
    public void testUpvotePost() {
        String userId = "testUser";
        UpvoteDTO upvoteDTO = new UpvoteDTO();
        upvoteDTO.setId(1);

        upvoteService.upvotePost(userId, upvoteDTO);

        Upvote upvote = upvoteService.getOne(new QueryWrapper<Upvote>().eq("user_id", userId).eq("post_id", upvoteDTO.getId()));
        assertNotNull(upvote);
        assertEquals(userId, upvote.getUserId());
        assertEquals(upvoteDTO.getId(), upvote.getPostId());
    }

    @Test
    public void testDownvotePost() {
        String userId = "testUser";
        UpvoteDTO upvoteDTO = new UpvoteDTO();
        upvoteDTO.setId(1);

        upvoteService.upvotePost(userId, upvoteDTO);
        upvoteService.downvotePost(userId, upvoteDTO);

        Upvote upvote = upvoteService.getOne(new QueryWrapper<Upvote>().eq("user_id", userId).eq("post_id", upvoteDTO.getId()));
        assertNull(upvote);
    }
}