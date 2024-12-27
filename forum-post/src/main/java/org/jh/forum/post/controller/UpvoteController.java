package org.jh.forum.post.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.constant.RedisKey;
import org.jh.forum.post.dto.ReplyDTO;
import org.jh.forum.post.dto.UpvoteDTO;
import org.jh.forum.post.feign.UserFeign;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.model.PostTag;
import org.jh.forum.post.model.Reply;
import org.jh.forum.post.model.Upvote;
import org.jh.forum.post.service.*;
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
    @Autowired
    private UserFeign userFeign;
    @Autowired
    private ITagService tagService;
    @Autowired
    private IPostTagService postTagService;

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
            taskService.delData(String.valueOf(upvoteDTO.getPostId()));
        }
    }

    @GetMapping("/upvote/list")
    public Pagination<PostVO> getPostUpvoteList(@RequestHeader("X-User-ID") String userId,
                                              @RequestParam(name = "pageNum", defaultValue = "1") Long pageNum,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        QueryWrapper<Upvote> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("created_on");
        Page<Upvote> upvotePage = upvoteService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Integer> postIds = upvotePage.getRecords().stream().map(Upvote::getPostId).collect(Collectors.toList());
        Long total = upvoteService.count(queryWrapper);
        Set<String> upvotePostIds = redisTemplate.opsForSet().members(RedisKey.USER_POST_UPVOTE + userId);
        List<PostVO> postVOs = new ArrayList<>();
        if (upvotePostIds == null) {
            upvotePostIds = new HashSet<>();
        }
        for (Integer postId : postIds) {
            Post post = postService.getById(postId);
            setPage(postVOs, upvotePostIds, post);
        }
        return Pagination.of(postVOs, upvotePage.getCurrent(), upvotePage.getSize(), total);
    }

    @PostMapping("/reply/upvote")
    public void upvoteReply(@RequestHeader("X-User-ID") String userId, @RequestBody ReplyDTO replyDTO) {
        Reply reply = replyService.getById(replyDTO.getReplyId());
        Set<String> upvoteReplyIds = redisTemplate.opsForSet().members(RedisKey.USER_REPLY_UPVOTE + userId);
        if (upvoteReplyIds == null) {
            upvoteReplyIds = new HashSet<>();
        }
        if (!upvoteReplyIds.contains(String.valueOf(reply.getId()))) {
            reply.setUpvoteCount(reply.getUpvoteCount() + 1);
            replyService.updateById(reply);
            redisTemplate.opsForSet().add(RedisKey.USER_REPLY_UPVOTE + userId, String.valueOf(replyDTO.getReplyId()));
        }
    }

    @PostMapping("/reply/downvote")
    public void downvoteReply(@RequestHeader("X-User-ID") String userId, @RequestBody ReplyDTO replyDTO) {
        Reply reply = replyService.getById(replyDTO.getReplyId());
        Set<String> upvoteReplyIds = redisTemplate.opsForSet().members(RedisKey.USER_REPLY_UPVOTE + userId);
        if (upvoteReplyIds == null) {
            upvoteReplyIds = new HashSet<>();
        }
        if (upvoteReplyIds.contains(String.valueOf(reply.getId()))) {
            reply.setUpvoteCount(reply.getUpvoteCount() - 1);
            replyService.updateById(reply);
            redisTemplate.opsForSet().remove(RedisKey.USER_REPLY_UPVOTE + userId, String.valueOf(replyDTO.getReplyId()));
        }
    }

    private void setPage(List<PostVO> postVOs, Set<String> collectedPostIds, Post post) {
        PostVO postVO = new PostVO();
        if (post == null) {
            postVOs.add(postVO);
            return;
        }
        postVO.setPostVO(post);
        Object user = userFeign.getUserById(post.getUserId());
        Map<String, Object> userMap = BeanUtil.beanToMap(user);
        postVO.setUserVO(userMap);
        if (collectedPostIds != null) {
            postVO.setIsCollect(collectedPostIds.contains(String.valueOf(post.getId())));
        } else {
            postVO.setIsCollect(false);
        }
        postVO.setIsUpvote(true);
        postVO.setTags(tagService.getTagsByPostTags(postTagService.list(new QueryWrapper<PostTag>().eq("post_id", post.getId()))));
        postVOs.add(postVO);
    }
}
