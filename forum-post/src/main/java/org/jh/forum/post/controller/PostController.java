package org.jh.forum.post.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jh.forum.post.dto.PostContentDTO;
import org.jh.forum.post.dto.PostDTO;
import org.jh.forum.post.feign.UserFeign;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.model.PostContent;
import org.jh.forum.post.service.ICategoryService;
import org.jh.forum.post.service.IPostContentService;
import org.jh.forum.post.service.IPostService;
import org.jh.forum.post.vo.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private IPostService postService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IPostContentService postContentService;

    @Autowired
    private UserFeign userFeign;

    @PostMapping("/add")
    public void addPost(@RequestHeader("X-User-ID") String userId, @RequestBody @Validated PostDTO postDTO) {
        Post post = new Post();
        post.setUserId(Long.valueOf(userId));
        post.setCategoryId(postDTO.getCategoryId());
        post.setTitle(postDTO.getTitle());
        post.setVisibility(postDTO.getVisibility());
        post.setIp(postDTO.getIp());
        post.setIpLoc(postDTO.getIpLoc());
        post.setCreatedOn(new Date());

        postService.save(post);
        Long postId = post.getId();

        // Loop through the content array in PostDTO
        for (PostContentDTO contentDTO : postDTO.getContent()) {
            PostContent postContent = new PostContent();
            postContent.setPostId(postId);
            postContent.setContent(contentDTO.getContent());
            postContent.setType(contentDTO.getType());
            postContent.setIsDel(false);

            // Save the post content
            postContentService.save(postContent);
        }

    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.removeById(id);
    }

    @PutMapping("/{id}")
    public void updatePost(@RequestBody @Validated Post post) {
        post.setModifiedOn(new Date());
        postService.updateById(post);
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getById(id);
    }

    @GetMapping("/list/category")
    public List<PostVO> getPostListByCategory(@RequestParam int pageNum, @RequestParam int pageSize, @RequestParam int categoryId) {
        IPage<Post> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        IPage<Post> userPage = postService.page(page, queryWrapper); // 调用 page 方法
        List<PostVO> postVOS = new ArrayList<>();
        for (Post post : userPage.getRecords()) {
            PostVO postVO = new PostVO();
            postVO.setPostVO(post);
            Object user = userFeign.getUserById(post.getUserId());
            Map<String, Object> userMap = BeanUtil.beanToMap(user);
            postVO.setUserVO(userMap);
            postVOS.add(postVO);
        }
        return postVOS;
    }

    @GetMapping("/single/get")
    public PostVO getSinglePost(@RequestParam Long postId) {
        PostVO postVO = new PostVO();
        Post post = postService.getById(postId);
        postVO.setPostVO(post);
        Object user = userFeign.getUserById(post.getUserId());
        Map<String, Object> userMap = BeanUtil.beanToMap(user);
        postVO.setUserVO(userMap);
        postVO.setPostContentVO(postContentService.list(new QueryWrapper<PostContent>().eq("post_id", postId)));
        return postVO;
    }
}