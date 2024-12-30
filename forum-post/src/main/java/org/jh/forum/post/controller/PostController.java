package org.jh.forum.post.controller;

import jakarta.validation.constraints.NotNull;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.dto.PostDTO;
import org.jh.forum.post.service.IPostService;
import org.jh.forum.post.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private IPostService postService;

    @PostMapping
    public void addPost(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated PostDTO postDTO) {
        postService.addPost(userId, postDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@RequestHeader("X-User-ID") String userId, @PathVariable("id") @NotNull(message = "帖子ID不能为空") Integer postId) {
        postService.deletePost(userId, postId);
    }

    @GetMapping("/{id}")
    public PostVO getPost(@RequestHeader("X-User-ID") String userId, @PathVariable("id") @NotNull(message = "帖子ID不能为空") Integer postId) {
        return postService.getPost(userId, postId);
    }

    @GetMapping("/list/category/{id}")
    public Pagination<PostVO> getPostListByCategory(@RequestHeader("X-User-ID") String userId,
                                                    @PathVariable("id") @NotNull(message = "帖子ID不能为空") Integer categoryId,
                                                    @RequestParam(name = "pageNum", defaultValue = "1") Long pageNum,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        return postService.getPostListByCategory(userId, categoryId, pageNum, pageSize);
    }

    @GetMapping("list/tag/{id}")
    public Pagination<PostVO> getPostListByTag(@RequestHeader("X-User-ID") String userId,
                                               @PathVariable("id") @NotNull(message = "帖子ID不能为空") Integer tagId,
                                               @RequestParam(name = "pageNum", defaultValue = "1") Long pageNum,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        return postService.getPostListByTag(userId, tagId, pageNum, pageSize);
    }

    @GetMapping("/list/user/{id}")
    public Pagination<PostVO> getPostListByUser(@RequestHeader("X-User-ID") String userId,
                                                @PathVariable("id") @NotNull(message = "用户ID不能为空") Integer authorId,
                                                @RequestParam(name = "pageNum", defaultValue = "1") Long pageNum,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        return postService.getPostListByUser(userId, authorId, pageNum, pageSize);
    }

    @GetMapping("/upvote/list")
    public Pagination<PostVO> getUpvotePostList(@RequestHeader("X-User-ID") String userId,
                                                @RequestParam(name = "pageNum", defaultValue = "1") Long pageNum,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        return postService.getUpvotePostList(userId, pageNum, pageSize);
    }

    @GetMapping("/collect/list")
    public Pagination<PostVO> getCollectPostList(@RequestHeader("X-User-ID") String userId,
                                                 @RequestParam(name = "pageNum", defaultValue = "1") Long pageNum,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        return postService.getCollectPostList(userId, pageNum, pageSize);
    }

    @GetMapping("/hot/day")
    public Pagination<PostVO> getHotPostOfDay(@RequestHeader("X-User-ID") String userId,
                                              @RequestParam(name = "pageNum", defaultValue = "1") Long pageNum,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Long pageSize) {
        return postService.getHotPostOfDay(userId, pageNum, pageSize);
    }
}