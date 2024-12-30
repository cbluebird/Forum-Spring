package org.jh.forum.post.controller;

import org.jh.forum.post.dto.UpvoteDTO;
import org.jh.forum.post.service.IUpvoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/post")
public class UpvoteController {
    @Autowired
    private IUpvoteService upvoteService;

    @PostMapping("/upvote")
    public void upvotePost(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated UpvoteDTO upvoteDTO) {
        upvoteService.upvotePost(userId, upvoteDTO);
    }

    @PostMapping("/downvote")
    public void downvotePost(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated UpvoteDTO upvoteDTO) {
        upvoteService.downvotePost(userId, upvoteDTO);
    }

    @PostMapping("/reply/upvote")
    public void upvoteReply(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated UpvoteDTO upvoteDTO) {
        upvoteService.upvoteReply(userId, upvoteDTO);
    }

    @PostMapping("/reply/downvote")
    public void downvoteReply(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated UpvoteDTO upvoteDTO) {
        upvoteService.downvoteReply(userId, upvoteDTO);
    }
}