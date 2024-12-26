package org.jh.forum.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.constant.RedisKey;
import org.jh.forum.post.dto.CollectDTO;
import org.jh.forum.post.model.Collection;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.service.ICollectionService;
import org.jh.forum.post.service.IPostService;
import org.jh.forum.post.service.impl.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        }
    }

    @GetMapping("/get")
    public Pagination<Post> getPostCollectionPage(@RequestParam Long pageNum, @RequestParam Long pageSize, @RequestHeader("X-User-ID") String userId) {
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("created_on");

        Page<Collection> collectionPage = collectionService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Integer> postIds = collectionPage.getRecords().stream().map(Collection::getPostId).collect(Collectors.toList());
        Long total = collectionService.count(queryWrapper);
        return Pagination.of(postService.listByIds(postIds), collectionPage.getCurrent(), collectionPage.getSize(), total);
    }
}