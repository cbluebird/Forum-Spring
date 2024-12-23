package org.jh.forum.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.dto.StarDTO;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.model.Star;
import org.jh.forum.post.service.IPostService;
import org.jh.forum.post.service.IStarService;
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
@RequestMapping("/api/post/star")
public class StarController {

    @Autowired
    private IStarService starService;

    @Autowired
    private IPostService postService;

    @Autowired
    private Jedis jedis;

    @Autowired
    private TaskService taskService;

    @PostMapping("/add")
    public void likePost(@RequestBody StarDTO starReq, @RequestHeader("X-User-ID") String userId) {
        Post post = postService.getById(starReq.getPostId());
        Long userIdLong = Long.valueOf(userId);
        QueryWrapper<Star> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", starReq.getPostId()).eq("user_id", userIdLong);

        Star existingStar = starService.getOne(queryWrapper);
        if (existingStar == null) {
            Star star = new Star();
            star.setPostId(starReq.getPostId());
            star.setUserId(userIdLong);
            star.setCreatedOn(new Date());
            starService.save(star);

            post.setUpvoteCount(post.getUpvoteCount() + 1);
            postService.updateById(post);
            jedis.sadd(userId + "-star", String.valueOf(starReq.getPostId()));
            taskService.setData(String.valueOf(starReq.getPostId()));
        }
    }

    @PostMapping("/del")
    public void unlikePost(@RequestBody StarDTO starReq, @RequestHeader("X-User-ID") String userId) {
        Long userIdLong = Long.valueOf(userId);
        QueryWrapper<Star> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", starReq.getPostId()).eq("user_id", userIdLong);

        Post post = postService.getById(starReq.getPostId());

        Star existingStar = starService.getOne(queryWrapper);
        if (existingStar != null) {
            starService.remove(queryWrapper);

            post.setUpvoteCount(post.getUpvoteCount() - 1);
            postService.updateById(post);
            jedis.srem(userId + "-star", String.valueOf(post.getId()));
        }
    }

    @GetMapping("/get")
    public Pagination<Post> getPostStarPage(@RequestParam Long pageNum, @RequestParam Long pageSize, @RequestHeader("X-User-ID") String userId) {
        Long userIdLong = Long.valueOf(userId);
        QueryWrapper<Star> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userIdLong).orderByDesc("created_on");

        Page<Star> starPage = starService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Long> postIds = starPage.getRecords().stream().map(Star::getPostId).collect(Collectors.toList());
        Long total = starService.count(queryWrapper);
        return Pagination.of(postService.listByIds(postIds), starPage.getCurrent(), starPage.getSize(), total);
    }
}
