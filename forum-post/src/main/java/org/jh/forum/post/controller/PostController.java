package org.jh.forum.post.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.post.dto.PostContentDTO;
import org.jh.forum.post.dto.PostDTO;
import org.jh.forum.post.dto.PostIdDTO;
import org.jh.forum.post.feign.UserFeign;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.model.PostContent;
import org.jh.forum.post.model.PostTag;
import org.jh.forum.post.service.*;
import org.jh.forum.post.service.impl.TaskService;
import org.jh.forum.post.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.*;

@Validated
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private IPostService postService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IPostContentService postContentService;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private Jedis jedis;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IPostTagService postTagService;

    @Autowired
    private ITagService tagService;

    @PostMapping("/add")
    public void addPost(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated PostDTO postDTO) {
        Post post = new Post();
        post.setUserId(Long.valueOf(userId));
        post.setCategoryId(postDTO.getCategoryId());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setVisibility(postDTO.getVisibility());
        post.setIp(postDTO.getIp());
        post.setIpLoc(postDTO.getIpLoc());
        post.setCreatedOn(new Date());

        postService.save(post);
        Long postId = post.getId();

        for (PostContentDTO contentDTO : postDTO.getLink()) {
            PostContent postContent = new PostContent();
            postContent.setPostId(postId);
            postContent.setContent(contentDTO.getContent());
            postContent.setType(contentDTO.getType());
            postContent.setIsDel(false);

            postContentService.save(postContent);
        }

        for (Long tagId : postDTO.getTags()) {
            PostTag postTag = new PostTag();
            postTag.setPostId(postId);
            postTag.setTagId(tagId);
            postTagService.save(postTag);
        }
    }

    @PostMapping("/del")
    public void deletePost(@RequestBody PostIdDTO id) {
        if (!postService.removeById(id.getId())) {
            throw new BizException(ErrorCode.POST_NOT_FOUND, "Failed to delete post with ID");
        }
    }

    @PutMapping("/{id}")
    public void updatePost(@RequestBody @Validated Post post) {
        post.setModifiedOn(new Date());
        if (!postService.updateById(post)) {
            throw new BizException(ErrorCode.POST_UPDATE_FAILED, "Failed to update post with ID: " + post.getId());
        }
    }

    @GetMapping("/list/category")
    public Pagination<PostVO> getPostListByCategory(@RequestHeader("X-User-ID") String userId, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam int categoryId) {
        IPage<Post> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        IPage<Post> userPage = postService.page(page, queryWrapper); // 调用 page 方法
        List<PostVO> postVOS = new ArrayList<>();
        Set<String> collectedPostIds = null;
        if (jedis.exists(userId + "-collect")) {
            collectedPostIds = jedis.smembers(userId + "-collect");
        }
        Set<String> upvotedPostIds = jedis.smembers(userId + "-star");
        if (jedis.exists(userId + "-star")) {
            upvotedPostIds = jedis.smembers(userId + "-star");
        }

        for (Post post : userPage.getRecords()) {
            setPage(postVOS, collectedPostIds, upvotedPostIds, post);
        }
        Long total = postService.count(queryWrapper);
        return Pagination.of(postVOS, userPage.getCurrent(), userPage.getSize(), total);
    }

    @GetMapping("/single/get")
    public PostVO getSinglePost(@RequestParam Long postId) {
        PostVO postVO = new PostVO();
        Post post = postService.getById(postId);
        if (post == null) {
            throw new BizException(ErrorCode.POST_NOT_FOUND, "Post not found with ID: " + postId);
        }
        postVO.setPostVO(post);
        Object user = userFeign.getUserById(post.getUserId());
        Map<String, Object> userMap = BeanUtil.beanToMap(user);
        postVO.setUserVO(userMap);
        postVO.setPostContentVO(postContentService.list(new QueryWrapper<PostContent>().eq("post_id", postId)));
        postVO.setTags(tagService.getTagsByPostTags(postTagService.list(new QueryWrapper<PostTag>().eq("post_id", post.getId()))));
        return postVO;
    }

    @GetMapping("/hot/day")
    public Pagination<PostVO> getHotPostListOfDay(@RequestHeader("X-User-ID") String userId, @RequestParam Long pageNum, @RequestParam Long pageSize) {
        List<String> postIds = jedis.zrevrange(taskService.DAY_KEY, (pageNum - 1) * pageSize, pageNum * pageSize - 1);
        List<PostVO> postVOS = new ArrayList<>();
        Set<String> collectedPostIds = null;
        if (jedis.exists(userId + "-collect")) {
            collectedPostIds = jedis.smembers(userId + "-collect");
        }
        Set<String> upvotedPostIds = jedis.smembers(userId + "-star");
        if (jedis.exists(userId + "-star")) {
            upvotedPostIds = jedis.smembers(userId + "-star");
        }
        for (String postId : postIds) {
            Post post = postService.getById(Long.valueOf(postId));
            if (post == null) {
                throw new BizException(ErrorCode.POST_NOT_FOUND, "Post not found with ID: " + postId);
            }
            setPage(postVOS, collectedPostIds, upvotedPostIds, post);
        }
        return Pagination.of(postVOS, pageNum, pageSize, (long) postIds.size());
    }

    private void setPage(List<PostVO> postVOS, Set<String> collectedPostIds, Set<String> upvotePostIds, Post post) {
        PostVO postVO = new PostVO();
        postVO.setPostVO(post);
        Object user = userFeign.getUserById(post.getUserId());
        Map<String, Object> userMap = BeanUtil.beanToMap(user);
        postVO.setUserVO(userMap);
        if (collectedPostIds != null) {
            postVO.setIsCollect(collectedPostIds.contains(String.valueOf(post.getId())));
        } else {
            postVO.setIsCollect(false);
        }
        if (upvotePostIds != null) {
            postVO.setIsUpvote(upvotePostIds.contains(String.valueOf(post.getId())));
        } else {
            postVO.setIsUpvote(false);
        }
        postVO.setTags(tagService.getTagsByPostTags(postTagService.list(new QueryWrapper<PostTag>().eq("post_id", post.getId()))));
        postVOS.add(postVO);
    }
}