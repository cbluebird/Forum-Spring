package org.jh.forum.post.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.post.constant.RedisKey;
import org.jh.forum.post.dto.CollectDTO;
import org.jh.forum.post.feign.UserFeign;
import org.jh.forum.post.model.Collection;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.model.PostTag;
import org.jh.forum.post.service.ICollectionService;
import org.jh.forum.post.service.IPostService;
import org.jh.forum.post.service.IPostTagService;
import org.jh.forum.post.service.ITagService;
import org.jh.forum.post.service.impl.TaskService;
import org.jh.forum.post.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/post/collect")
public class CollectionController {
    @Autowired
    private ICollectionService collectionService;

    @Autowired
    private IPostService postService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private ITagService tagService;

    @Autowired
    private IPostTagService postTagService;

    @PostMapping("/add")
    public void addCollection(@RequestBody CollectDTO collectReq, @RequestHeader("X-User-ID") String userId) {
        Post post = postService.getById(collectReq.getPostId());
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", collectReq.getPostId()).eq("user_id", userId);

        Collection existingCollection = collectionService.getOne(queryWrapper);
        if (existingCollection == null) {
            Collection collection = new Collection();
            collection.setPostId(collectReq.getPostId());
            collection.setUserId(Integer.valueOf(userId));
            collectionService.save(collection);

            post.setCollectionCount(post.getCollectionCount() + 1);
            postService.updateById(post);
            redisTemplate.opsForSet().add(RedisKey.USER_COLLECTION + userId, String.valueOf(collectReq.getPostId()));
            taskService.setData(String.valueOf(collectReq.getPostId()));
        }
    }

    @PostMapping("/del")
    public void removeCollection(@RequestBody CollectDTO collectReq, @RequestHeader("X-User-ID") String userId) {
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", collectReq.getPostId()).eq("user_id", userId);

        Post post = postService.getById(collectReq.getPostId());

        Collection existingCollection = collectionService.getOne(queryWrapper);
        if (existingCollection != null) {
            collectionService.remove(queryWrapper);

            post.setCollectionCount(post.getCollectionCount() - 1);
            postService.updateById(post);
            redisTemplate.opsForSet().remove(RedisKey.USER_COLLECTION + userId, String.valueOf(collectReq.getPostId()));
            taskService.delData(String.valueOf(collectReq.getPostId()));
        }
    }

    @GetMapping("/get")
    public Pagination<PostVO> getPostCollectionPage(@RequestParam Long pageNum, @RequestParam Long pageSize, @RequestHeader("X-User-ID") String userId) {
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("created_on");
        Page<Collection> collectionPage = collectionService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Integer> postIds = collectionPage.getRecords().stream().map(Collection::getPostId).collect(Collectors.toList());
        Long total = collectionService.count(queryWrapper);
        Set<String> collectedPostIds = redisTemplate.opsForSet().members(RedisKey.USER_POST_UPVOTE + userId);
        List<PostVO> postVOs = new ArrayList<>();
        if (collectedPostIds == null) {
            collectedPostIds = new HashSet<>();
        }
        for (Integer postId : postIds) {
            Post post = postService.getById(postId);
            if (post == null) {
                throw new BizException(ErrorCode.POST_NOT_FOUND, "Post not found with ID: " + postId);
            }
            setPage(postVOs, collectedPostIds, post);
        }
        return Pagination.of(postVOs, collectionPage.getCurrent(), collectionPage.getSize(), total);
    }

    private void setPage(List<PostVO> postVOs, Set<String> upvotePostIds, Post post) {
        PostVO postVO = new PostVO();
        postVO.setPostVO(post);
        Object user = userFeign.getUserById(post.getUserId());
        Map<String, Object> userMap = BeanUtil.beanToMap(user);
        postVO.setUserVO(userMap);
        postVO.setIsCollect(true);
        if (upvotePostIds != null) {
            postVO.setIsUpvote(upvotePostIds.contains(String.valueOf(post.getId())));
        } else {
            postVO.setIsUpvote(false);
        }
        postVO.setTags(tagService.getTagsByPostTags(postTagService.list(new QueryWrapper<PostTag>().eq("post_id", post.getId()))));
        postVOs.add(postVO);
    }
}