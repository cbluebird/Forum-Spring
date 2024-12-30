package org.jh.forum.post.controller;

import org.jh.forum.post.dto.CollectDTO;
import org.jh.forum.post.service.ICollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/post")
public class CollectController {
    @Autowired
    private ICollectService collectService;

    @PostMapping("/collect")
    public void collectPost(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated CollectDTO collectDTO) {
        collectService.collectPost(userId, collectDTO);
    }

    @PostMapping("/uncollect")
    public void uncollectPost(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated CollectDTO collectDTO) {
        collectService.uncollectPost(userId, collectDTO);
    }
}