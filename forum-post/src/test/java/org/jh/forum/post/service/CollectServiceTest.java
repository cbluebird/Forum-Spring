package org.jh.forum.post.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jh.forum.post.dto.CollectDTO;
import org.jh.forum.post.model.Collect;
import org.jh.forum.post.service.impl.CollectServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CollectServiceTest {

    @Autowired
    private CollectServiceImpl collectService;

    @Test
    public void testCollectPost() {
        String userId = "testUser";
        CollectDTO collectDTO = new CollectDTO();
        collectDTO.setId(1);

        collectService.collectPost(userId, collectDTO);

        Collect collect = collectService.getOne(new QueryWrapper<Collect>().eq("user_id", userId).eq("post_id", collectDTO.getId()));
        assertNotNull(collect);
        assertEquals(userId, collect.getUserId());
        assertEquals(collectDTO.getId(), collect.getPostId());
    }

    @Test
    public void testUncollectPost() {
        String userId = "testUser";
        CollectDTO collectDTO = new CollectDTO();
        collectDTO.setId(1);

        collectService.collectPost(userId, collectDTO);
        collectService.uncollectPost(userId, collectDTO);

        Collect collect = collectService.getOne(new QueryWrapper<Collect>().eq("user_id", userId).eq("post_id", collectDTO.getId()));
        assertNull(collect);
    }
}