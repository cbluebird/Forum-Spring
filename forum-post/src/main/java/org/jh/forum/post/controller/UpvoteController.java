package org.jh.forum.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.constant.RedisKey;
import org.jh.forum.post.dto.ReplyUpvoteDTO;
import org.jh.forum.post.dto.UpvoteDTO;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.model.Reply;
import org.jh.forum.post.model.Upvote;
import org.jh.forum.post.service.IPostService;
import org.jh.forum.post.service.IReplyService;
import org.jh.forum.post.service.IUpvoteService;
import org.jh.forum.post.service.impl.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/post")
public class UpvoteController {
    @Autowired
    private IUpvoteService upvoteService;
    @Autowired
    private IPostService postService;
    @Autowired
    private IReplyService replyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/upvote")
    public void upvotePost(@RequestHeader("X-User-ID") String userId, @RequestBody UpvoteDTO upvoteDTO) {
        Post post = postService.getById(upvoteDTO.getPostId());
        QueryWrapper<Upvote> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", upvoteDTO.getPostId()).eq("user_id", userId);
        if (upvoteService.getOne(queryWrapper) == null) {
            Upvote upvote = new Upvote();
            upvote.setPostId(upvoteDTO.getPostId());
            upvote.setUserId(Integer.valueOf(userId));
            upvoteService.save(upvote);
            post.setUpvoteCount(post.getUpvoteCount() + 1);
            postService.updateById(post);
            redisTemplate.opsForSet().add(RedisKey.USER_POST_UPVOTE + userId, String.valueOf(upvoteDTO.getPostId()));
            taskService.setData(String.valueOf(upvoteDTO.getPostId()));
        }
    }

    @PostMapping("/downvote")
    public void downvotePost(@RequestHeader("X-User-ID") String userId, @RequestBody UpvoteDTO upvoteDTO) {
        Post post = postService.getById(upvoteDTO.getPostId());
        QueryWrapper<Upvote> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", upvoteDTO.getPostId()).eq("user_id", userId);
        if (upvoteService.getOne(queryWrapper) != null) {
            upvoteService.remove(queryWrapper);
            post.setUpvoteCount(post.getUpvoteCount() - 1);
            postService.updateById(post);
            redisTemplate.opsForSet().remove(RedisKey.USER_POST_UPVOTE + userId, String.valueOf(upvoteDTO.getPostId()));
        }
    }

    @GetMapping("/upvote/list")
    public Pagination<Post> getPostUpvoteList(@RequestHeader("X-User-ID") String userId,
                                              @RequestParam(name = "pageNum", defaultValue = "1") Long pageNum,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        QueryWrapper<Upvote> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("created_on");
        Page<Upvote> upvotePage = upvoteService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Integer> postIds = upvotePage.getRecords().stream().map(Upvote::getPostId).collect(Collectors.toList());
        return Pagination.of(postService.listByIds(postIds), upvotePage.getCurrent(), upvotePage.getSize(), upvotePage.getTotal());
    }

    @PostMapping("/reply/upvote")
    public void upvoteReply(@RequestHeader("X-User-ID") String userId, @RequestBody ReplyUpvoteDTO replyUpvoteDTO) {
        Reply reply = replyService.getById(replyUpvoteDTO.getReplyId());
        Set<String> upvoteReplyIds = redisTemplate.opsForSet().members(RedisKey.USER_REPLY_UPVOTE + userId);
        if (upvoteReplyIds == null) {
            upvoteReplyIds = new HashSet<>();
        }
        if (!upvoteReplyIds.contains(String.valueOf(reply.getId()))) {
            reply.setUpvoteCount(reply.getUpvoteCount() + 1);
            replyService.updateById(reply);
            redisTemplate.opsForSet().add(RedisKey.USER_REPLY_UPVOTE + userId, String.valueOf(replyUpvoteDTO.getReplyId()));
        }
    }

    @PostMapping("/reply/downvote")
    public void downvoteReply(@RequestHeader("X-User-ID") String userId, @RequestBody ReplyUpvoteDTO replyUpvoteDTO) {
        Reply reply = replyService.getById(replyUpvoteDTO.getReplyId());
        Set<String> upvoteReplyIds = redisTemplate.opsForSet().members(RedisKey.USER_REPLY_UPVOTE + userId);
        if (upvoteReplyIds == null) {
            upvoteReplyIds = new HashSet<>();
        }
        if (upvoteReplyIds.contains(String.valueOf(reply.getId()))) {
            reply.setUpvoteCount(reply.getUpvoteCount() - 1);
            replyService.updateById(reply);
            redisTemplate.opsForSet().remove(RedisKey.USER_REPLY_UPVOTE + userId, String.valueOf(replyUpvoteDTO.getReplyId()));
        }
    }
}
