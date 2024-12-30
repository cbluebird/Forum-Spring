package org.jh.forum.post.controller;

import jakarta.validation.constraints.NotNull;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.dto.ReplyDTO;
import org.jh.forum.post.service.IReplyService;
import org.jh.forum.post.vo.ReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/post/reply")
public class ReplyController {
    @Autowired
    private IReplyService replyService;

    @PostMapping
    public void addReply(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated ReplyDTO replyDTO) {
        replyService.addReply(userId, replyDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteReply(@RequestHeader("X-User-ID") String userId, @PathVariable("id") @NotNull(message = "回复ID不能为空") Integer replyId) {
        replyService.deleteReply(userId, replyId);
    }

    @GetMapping("/list")
    public Pagination<ReplyVO> getReplyList(@RequestHeader("X-User-ID") String userId, @RequestParam Integer postId) {
        return replyService.getReplyList(userId, postId);
    }
}