package org.jh.forum.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.dto.ReplyStarDTO;
import org.jh.forum.post.model.Reply;
import org.jh.forum.post.service.IReplyService;
import org.jh.forum.post.vo.ReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/api/post/reply")
public class ReplyController {

    @Autowired
    private IReplyService replyService;

    @Autowired
    private Jedis jedis;

    @PostMapping("/add")
    public void addReply(@RequestBody Reply reply, @RequestHeader("X-User-ID") String userId) {
        reply.setUserId(Long.valueOf(userId));
        reply.setCreatedOn(new Date());
        replyService.save(reply);
    }

    @DeleteMapping("/del")
    public void deleteReply(@RequestParam Long replyId) {
        replyService.removeById(replyId);
    }

    @PostMapping("/like")
    public void likeReply(@RequestHeader("X-User-ID") String userId, @RequestBody ReplyStarDTO replyStarDTO) {
        Reply reply = replyService.getById(replyStarDTO.getReplyId());
        if (reply != null) {
            reply.setThumbsUpCount(reply.getThumbsUpCount() + 1);
            replyService.updateById(reply);
            jedis.sadd(userId + "-reply-star", String.valueOf(replyStarDTO.getReplyId()));
        }
    }

    @PostMapping("/unlike")
    public void unlikeReply(@RequestHeader("X-User-ID") String userId, @RequestBody ReplyStarDTO replyStarDTO) {
        Reply reply = replyService.getById(replyStarDTO.getReplyId());
        Set<String> upvotedPostIds = jedis.smembers(userId + "-reply-star");
        if (upvotedPostIds.contains(String.valueOf(reply.getId()))) {
            reply.setThumbsUpCount(reply.getThumbsUpCount() - 1);
            replyService.updateById(reply);
            jedis.srem(userId + "-reply-star", String.valueOf(replyStarDTO.getReplyId()));
        }
    }

    @GetMapping("/get")
    public Pagination<ReplyVO> getReplies(@RequestHeader("X-User-ID") String userId, @RequestParam Long postId) {
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId).orderByDesc("created_on");
        Set<String> upvotedPostIds = jedis.smembers(userId + "-reply-star");
        List<Reply> replies = replyService.list(queryWrapper);
        List<ReplyVO> replyVOArrayList = new ArrayList<>();
        for (Reply reply : replies) {
            ReplyVO replyVO = new ReplyVO();
            replyVO.setReplyVO(reply);
            replyVO.setIsUpvote(upvotedPostIds.contains(reply.getId().toString()));
            replyVOArrayList.add(replyVO);
        }
        return Pagination.of(replyVOArrayList, 1L, 10L, (long) replyVOArrayList.size());
    }
}