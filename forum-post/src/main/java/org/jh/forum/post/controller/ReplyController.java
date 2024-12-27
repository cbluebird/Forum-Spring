package org.jh.forum.post.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.constant.RedisKey;
import org.jh.forum.post.dto.ReplyDTO;
import org.jh.forum.post.feign.UserFeign;
import org.jh.forum.post.model.Reply;
import org.jh.forum.post.service.IReplyService;
import org.jh.forum.post.vo.ReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Validated
@RestController
@RequestMapping("/api/post/reply")
public class ReplyController {
    @Autowired
    private IReplyService replyService;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @PostMapping("/add")
    public void addReply(@RequestBody Reply reply, @RequestHeader("X-User-ID") String userId) {
        reply.setUserId(Integer.valueOf(userId));
        if (reply.getRoot() != 0) {
            Reply root = replyService.getById(reply.getRoot());
            root.setReplyCount(root.getReplyCount() + 1);
            replyService.updateById(root);
        }
        if (reply.getParent() != 0 && !reply.getParent().equals(reply.getRoot())) {
            Reply parent = replyService.getById(reply.getParent());
            parent.setReplyCount(parent.getReplyCount() + 1);
            replyService.updateById(parent);
        }
        replyService.save(reply);
    }

    @PostMapping("/del")
    public void deleteReply(@RequestHeader("X-User-ID") String userId, @RequestBody ReplyDTO replyDTO) {
        replyService.remove(new QueryWrapper<Reply>().eq("id", replyDTO.getReplyId()).eq("user_id", userId));
        replyService.remove(new QueryWrapper<Reply>().eq("root", replyDTO.getReplyId()));
        replyService.remove(new QueryWrapper<Reply>().eq("parent", replyDTO.getReplyId()));
    }

    @GetMapping("/get")
    public Pagination<ReplyVO> getReplies(@RequestHeader("X-User-ID") String userId, @RequestParam Integer postId) {
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId).orderByDesc("created_on");
        Set<Integer> upvoteReplyIds = redisTemplate.opsForSet().members(RedisKey.USER_REPLY_UPVOTE + userId);
        if (upvoteReplyIds == null) {
            upvoteReplyIds = new HashSet<>();
        }
        List<Reply> replies = replyService.list(queryWrapper);
        List<ReplyVO> replyVOs = new ArrayList<>();
        for (Reply reply : replies) {
            ReplyVO replyVO = new ReplyVO();
            replyVO.setReplyVO(reply);
            replyVO.setIsUpvote(upvoteReplyIds.contains(reply.getId()));
            Object user = userFeign.getUserById(reply.getUserId());
            Map<String, Object> userMap = BeanUtil.beanToMap(user);
            replyVO.setUserVO(userMap);
            replyVOs.add(replyVO);
        }
        return Pagination.of(replyVOs, 1L, 10L, (long) replyVOs.size());
    }
}