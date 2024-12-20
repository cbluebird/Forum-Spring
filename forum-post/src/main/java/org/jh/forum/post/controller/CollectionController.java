package org.jh.forum.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jh.forum.post.dto.CollectDTO;
import org.jh.forum.post.model.Collection;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.service.ICollectionService;
import org.jh.forum.post.service.IPostService;
import org.jh.forum.post.service.impl.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.Date;
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
    private Jedis jedis;

    @Autowired
    private TaskService taskService;

    @PostMapping("/add")
    public void addCollection(@RequestBody CollectDTO collectReq, @RequestHeader("X-User-ID") String userId) {
        Post post = postService.getById(collectReq.getPostId());
        Long userIdLong = Long.valueOf(userId);
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", collectReq.getPostId()).eq("user_id", userIdLong);

        Collection existingCollection = collectionService.getOne(queryWrapper);
        if (existingCollection == null) {
            Collection collection = new Collection();
            collection.setPostId(collectReq.getPostId());
            collection.setUserId(userIdLong);
            collection.setCreatedOn(new Date());
            collectionService.save(collection);

            post.setCollectionCount(post.getCollectionCount() + 1);
            postService.updateById(post);
            jedis.sadd(userId + "-collect", String.valueOf(collectReq.getPostId()));
            taskService.setData(String.valueOf(collectReq.getPostId()));
        }
    }

    @DeleteMapping("/del")
    public void removeCollection(@RequestBody Post post, @RequestHeader("X-User-ID") String userId) {
        Long userIdLong = Long.valueOf(userId);
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", post.getId()).eq("user_id", userIdLong);

        Collection existingCollection = collectionService.getOne(queryWrapper);
        if (existingCollection != null) {
            collectionService.remove(queryWrapper);

            post.setCollectionCount(post.getCollectionCount() - 1);
            postService.updateById(post);
            jedis.srem(userId + "-collect", String.valueOf(post.getId()));
        }
    }

    @GetMapping("/get")
    public List<Post> getPostCollectionPage(@RequestParam int page_num, @RequestParam int page_size, @RequestHeader("X-User-ID") String userId) {
        Long userIdLong = Long.valueOf(userId);
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userIdLong).orderByDesc("created_on");

        Page<Collection> collectionPage = collectionService.page(new Page<>(page_num, page_size), queryWrapper);
        List<Long> postIds = collectionPage.getRecords().stream().map(Collection::getPostId).collect(Collectors.toList());

        return postService.listByIds(postIds);
    }
}