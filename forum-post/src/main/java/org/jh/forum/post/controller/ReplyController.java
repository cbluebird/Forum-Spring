package org.jh.forum.post.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jh.forum.post.model.Reply;
import org.jh.forum.post.service.IReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/post/reply")
public class ReplyController {

    @Autowired
    private IReplyService replyService;

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
    public void likeReply(@RequestParam Long replyId) {
        Reply reply = replyService.getById(replyId);
        if (reply != null) {
            reply.setThumbsUpCount(reply.getThumbsUpCount() + 1);
            replyService.updateById(reply);
        }
    }

    @GetMapping("/get")
    public List<Reply> getReplies(@RequestParam Long postId) {
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId).orderByDesc("created_on");
        return replyService.list(queryWrapper);
    }
}