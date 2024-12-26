package org.jh.forum.post.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.post.constant.RedisKey;
import org.jh.forum.post.dto.PostDTO;
import org.jh.forum.post.dto.PostIdDTO;
import org.jh.forum.post.feign.UserFeign;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.model.PostTag;
import org.jh.forum.post.service.IPostService;
import org.jh.forum.post.service.IPostTagService;
import org.jh.forum.post.service.ITagService;
import org.jh.forum.post.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// TODO 根据userid查询用户的帖子列表
// TODO 删除帖子的同时删除评论
// TODO 按照tag 查询帖子
// TODO 热榜-
// TODO 点赞收藏的响应没有用户信息
// TODO 查询时候的事务
// TODO tag热榜


@Validated
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private IPostService postService;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private IPostTagService postTagService;

    @Autowired
    private ITagService tagService;

    @PostMapping("/add")
    public void addPost(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated PostDTO postDTO) {
        Post post = new Post();
        post.setUserId(Integer.valueOf(userId));
        post.setCategoryId(postDTO.getCategoryId());
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setVisibility(postDTO.getVisibility());
        post.setIp(postDTO.getIp());
        post.setIpLoc(postDTO.getIpLoc());

        postService.save(post);
        Integer postId = post.getId();

        if (postDTO.getTags() != null) {
            for (Integer tagId : postDTO.getTags()) {
                PostTag postTag = new PostTag();
                postTag.setPostId(postId);
                postTag.setTagId(tagId);
                postTagService.save(postTag);
            }
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
        if (!postService.updateById(post)) {
            throw new BizException(ErrorCode.POST_UPDATE_FAILED, "Failed to update post with ID: " + post.getId());
        }
    }

    @GetMapping("/list/category")
    public Pagination<PostVO> getPostListByCategory(@RequestHeader("X-User-ID") String userId, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam int categoryId) {
        IPage<Post> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        IPage<Post> userPage = postService.page(page, queryWrapper);
        List<PostVO> postVOs = new ArrayList<>();
        Set<String> upvotePostIds = redisTemplate.opsForSet().members(RedisKey.USER_POST_UPVOTE + userId);
        if (upvotePostIds == null) {
            upvotePostIds = new HashSet<>();
        }
        Set<String> collectPostIds = redisTemplate.opsForSet().members(RedisKey.USER_COLLECTION + userId);
        if (collectPostIds == null) {
            collectPostIds = new HashSet<>();
        }
        for (Post post : userPage.getRecords()) {
            setPage(postVOs, collectPostIds, upvotePostIds, post);
        }
        Long total = postService.count(queryWrapper);
        return Pagination.of(postVOs, userPage.getCurrent(), userPage.getSize(), total);
    }

    @GetMapping("/list/user")
    public Pagination<PostVO> getPostListByUser(@RequestHeader("X-User-ID") String userId, @RequestParam int pageNum, @RequestParam int pageSize, @RequestParam int authorId) {
        IPage<Post> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", authorId);
        IPage<Post> userPage = postService.page(page, queryWrapper);
        List<PostVO> postVOs = new ArrayList<>();
        Set<String> upvotePostIds = redisTemplate.opsForSet().members(RedisKey.USER_POST_UPVOTE + userId);
        if (upvotePostIds == null) {
            upvotePostIds = new HashSet<>();
        }
        Set<String> collectPostIds = redisTemplate.opsForSet().members(RedisKey.USER_COLLECTION + userId);
        if (collectPostIds == null) {
            collectPostIds = new HashSet<>();
        }
        for (Post post : userPage.getRecords()) {
            setPage(postVOs, collectPostIds, upvotePostIds, post);
        }
        Long total = postService.count(queryWrapper);
        return Pagination.of(postVOs, userPage.getCurrent(), userPage.getSize(), total);
    }

    @GetMapping("/single/get")
    public PostVO getSinglePost(@RequestParam Integer postId) {
        PostVO postVO = new PostVO();
        Post post = postService.getById(postId);
        if (post == null) {
            throw new BizException(ErrorCode.POST_NOT_FOUND, "Post not found with ID: " + postId);
        }
        post.setViewCount(post.getViewCount() + 1);
        postService.updateById(post);
        postVO.setPostVO(post);
        Object user = userFeign.getUserById(post.getUserId());
        Map<String, Object> userMap = BeanUtil.beanToMap(user);
        postVO.setUserVO(userMap);
        postVO.setTags(tagService.getTagsByPostTags(postTagService.list(new QueryWrapper<PostTag>().eq("post_id", post.getId()))));
        return postVO;
    }

    @GetMapping("/hot/day")
    public Pagination<PostVO> getHotPostListOfDay(@RequestHeader("X-User-ID") String userId, @RequestParam Long pageNum, @RequestParam Long pageSize) {
        Set<String> postIds = redisTemplate.opsForZSet().reverseRange(RedisKey.HOT_POST_DAY, (pageNum - 1) * pageSize, pageNum * pageSize - 1);
        if (postIds == null) {
            return Pagination.of(new ArrayList<>(), pageNum, pageSize, 0L);
        }
        List<PostVO> postVOs = new ArrayList<>();
        Set<String> upvotePostIds = redisTemplate.opsForSet().members(RedisKey.USER_POST_UPVOTE + userId);
        if (upvotePostIds == null) {
            upvotePostIds = new HashSet<>();
        }
        Set<String> collectPostIds = redisTemplate.opsForSet().members(RedisKey.USER_COLLECTION + userId);
        if (collectPostIds == null) {
            collectPostIds = new HashSet<>();
        }
        for (String postId : postIds) {
            Post post = postService.getById(postId);
            if (post == null) {
                throw new BizException(ErrorCode.POST_NOT_FOUND, "Post not found with ID: " + postId);
            }
            setPage(postVOs, collectPostIds, upvotePostIds, post);
        }
        return Pagination.of(postVOs, pageNum, pageSize, (long) postIds.size());
    }

    private void setPage(List<PostVO> postVOs, Set<String> collectedPostIds, Set<String> upvotePostIds, Post post) {
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
        postVOs.add(postVO);
    }
}