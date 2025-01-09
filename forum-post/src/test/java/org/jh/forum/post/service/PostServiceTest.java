package org.jh.forum.post.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.dto.PostDTO;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.service.impl.PostServiceImpl;
import org.jh.forum.post.vo.PostVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostServiceImpl postService;

    @Test
    public void testAddPost() {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Test Post");
        postDTO.setContent("This is a test post.");
        postDTO.setTagIds(Set.of(1, 2, 3));
        postService.addPost("1", postDTO);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<Post>().eq("title", "Test Post");
        Post post = postService.getOne(queryWrapper);
        assertNotNull(post);
        assertEquals("Test Post", post.getTitle());
    }

    @Test
    public void testDeletePost() {
        postService.deletePost("1", 1);
        assertNull(postService.getById(1));
    }

    @Test
    public void testGetPost() {
        PostVO postVO = postService.getPost("1", 1);
        assertNotNull(postVO);
        assertEquals(1, postVO.getId());
    }

    @Test
    public void testGetPostListByCategory() {
        Pagination<PostVO> pagination = postService.getPostListByCategory("1", 1, 1L, 10L);
        assertNotNull(pagination);
        assertFalse(pagination.getList().isEmpty());
    }

    @Test
    public void testGetPostListByTag() {
        Pagination<PostVO> pagination = postService.getPostListByTag("1", 1, 1L, 10L);
        assertNotNull(pagination);
        assertFalse(pagination.getList().isEmpty());
    }

    @Test
    public void testGetPostListByUser() {
        Pagination<PostVO> pagination = postService.getPostListByUser("1", 1, 1L, 10L);
        assertNotNull(pagination);
        assertFalse(pagination.getList().isEmpty());
    }

    @Test
    public void testGetUpvotePostList() {
        Pagination<PostVO> pagination = postService.getUpvotePostList("1", 1L, 10L);
        assertNotNull(pagination);
        assertFalse(pagination.getList().isEmpty());
    }

    @Test
    public void testGetCollectPostList() {
        Pagination<PostVO> pagination = postService.getCollectPostList("1", 1L, 10L);
        assertNotNull(pagination);
        assertFalse(pagination.getList().isEmpty());
    }

    @Test
    public void testGetHotPostOfDay() {
        Pagination<PostVO> pagination = postService.getHotPostOfDay("1", 1L, 10L);
        assertNotNull(pagination);
        assertFalse(pagination.getList().isEmpty());
    }

    @Test
    public void testEnsurePostExist() {
        Post post = postService.ensurePostExist(1);
        assertNotNull(post);
        assertEquals(1, post.getId());
    }
}