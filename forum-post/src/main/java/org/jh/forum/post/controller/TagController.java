package org.jh.forum.post.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.post.constant.RedisKey;
import org.jh.forum.post.feign.UserFeign;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.model.PostTag;
import org.jh.forum.post.model.Tag;
import org.jh.forum.post.service.IPostService;
import org.jh.forum.post.service.IPostTagService;
import org.jh.forum.post.service.ITagService;
import org.jh.forum.post.vo.PostTagVO;
import org.jh.forum.post.vo.PostVO;
import org.jh.forum.post.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Validated
@RestController
@RequestMapping("/api/post/tag")
public class TagController {
    @Autowired
    private IPostService postService;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    private IPostTagService postTagService;

    @Autowired
    private ITagService tagService;

    @PostMapping("/add")
    public void addTag(@RequestBody Map<String, String> requestBody, @RequestHeader("X-User-ID") String userId) {
        String tagName = requestBody.get("name");
        if (tagName != null && !tagName.isEmpty()) {
            Tag tag = new Tag();
            tag.setUserId(Integer.valueOf(userId));
            tag.setTag(tagName);
            tagService.save(tag);
        }
    }

    @GetMapping("/get")
    public PostTagVO getTagById(@RequestParam Integer tagId) {
        Tag tag = tagService.getById(tagId);
        if (tag == null) {
            throw new BizException(ErrorCode.POST_TAG_NOT_FOUND, "Tag not found with ID: " + tagId);
        }
        PostTagVO tagVO = new PostTagVO();
        tagVO.setId(tag.getId());
        tagVO.setTag(tag.getTag());
        tagVO.setCount(postTagService.count(new QueryWrapper<PostTag>().eq("tag_id", tag.getId())));
        return tagVO;
    }

    @GetMapping("/list")
    public Pagination<PostVO> getPostsByTag(@RequestHeader("X-User-ID") String userId, @RequestParam Integer tagId, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        QueryWrapper<PostTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_id", tagId);
        Page<PostTag> postTagPage = postTagService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Integer> postIds = postTagPage.getRecords().stream().map(PostTag::getPostId).toList();
        Long total = postTagService.count(queryWrapper);
        List<PostVO> postVOs = new ArrayList<>();
        Set<Integer> collectedPostIds = redisTemplate.opsForSet().members(RedisKey.USER_POST_UPVOTE + userId);
        if (collectedPostIds == null) {
            collectedPostIds = new HashSet<>();
        }
        Set<Integer> upvotePostIds = redisTemplate.opsForSet().members(RedisKey.USER_POST_UPVOTE + userId);
        if (upvotePostIds == null) {
            upvotePostIds = new HashSet<>();
        }
        for (Integer postId : postIds) {
            Post post = postService.getById(postId);
            if (post == null) {
                throw new BizException(ErrorCode.POST_NOT_FOUND, "Post not found with ID: " + postId);
            }
            setPage(postVOs, collectedPostIds, upvotePostIds, post);
        }
        return Pagination.of(postVOs, postTagPage.getCurrent(), postTagPage.getSize(), total);
    }

    @GetMapping("/hot/day")
    public Pagination<TagVO> getHotTagListOfDay(@RequestParam Long pageNum, @RequestParam Long pageSize) {
        Set<Integer> tagIds = redisTemplate.opsForZSet().reverseRange(RedisKey.HOT_TAG_DAY, (pageNum - 1) * pageSize, pageNum * pageSize - 1);
        if (tagIds == null) {
            return Pagination.of(new ArrayList<>(), pageNum, pageSize, 0L);
        }
        List<TagVO> tagVOs = new ArrayList<>();
        for (Integer tagId : tagIds) {
            Tag tag = tagService.getById(tagId);
            if (tag == null) {
                throw new BizException(ErrorCode.POST_TAG_NOT_FOUND, "Tag not found with ID: " + tagId);
            }
            TagVO tagVO = new TagVO();
            BeanUtil.copyProperties(tag, tagVO);
            tagVOs.add(tagVO);
        }
        return Pagination.of(tagVOs, pageNum, pageSize, (long) tagIds.size());
    }

    private void setPage(List<PostVO> postVOs, Set<Integer> collectedPostIds, Set<Integer> upvotePostIds, Post post) {
        PostVO postVO = new PostVO();
        postVO.setPostVO(post);
        Object user = userFeign.getUserById(post.getUserId());
        Map<String, Object> userMap = BeanUtil.beanToMap(user);
        postVO.setUserVO(userMap);
        if (collectedPostIds != null) {
            postVO.setIsCollect(collectedPostIds.contains(post.getId()));
        } else {
            postVO.setIsCollect(false);
        }
        if (upvotePostIds != null) {
            postVO.setIsUpvote(upvotePostIds.contains(post.getId()));
        } else {
            postVO.setIsUpvote(false);
        }
        postVO.setTags(tagService.getTagsByPostTags(postTagService.list(new QueryWrapper<PostTag>().eq("post_id", post.getId()))));
        postVOs.add(postVO);
    }
}
