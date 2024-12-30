package org.jh.forum.post.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.executor.BatchResult;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.common.exception.SystemException;
import org.jh.forum.post.constant.RedisKey;
import org.jh.forum.post.dto.PostDTO;
import org.jh.forum.post.dto.UserDTO;
import org.jh.forum.post.feign.UserFeign;
import org.jh.forum.post.mapper.*;
import org.jh.forum.post.model.*;
import org.jh.forum.post.service.ICategoryService;
import org.jh.forum.post.service.IPostService;
import org.jh.forum.post.service.ITagService;
import org.jh.forum.post.service.IUserService;
import org.jh.forum.post.vo.PostVO;
import org.jh.forum.post.vo.TagVO;
import org.jh.forum.post.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements IPostService {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ITagService tagService;
    @Autowired
    private IUserService userService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private PostTagMapper postTagMapper;
    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private UpvoteMapper upvoteMapper;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    @Autowired
    private UserFeign userFeign;

    @Override
    public void addPost(String userId, PostDTO postDTO) {
        Set<Integer> tagIds = Optional.ofNullable(postDTO.getTagIds()).orElse(Set.of());
        if (!tagIds.isEmpty() && tagMapper.selectByIds(tagIds).size() != tagIds.size()) {
            throw new BizException(ErrorCode.POST_TAG_NOT_FOUND, "标签IDs: " + tagIds + " 中有不存在的标签");
        }
        Post post = new Post();
        BeanUtil.copyProperties(postDTO, post);
        post.setUserId(Integer.valueOf(userId));
        if (postMapper.insert(post) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "添加帖子: " + post + " 失败");
        }
        Integer postId = post.getId();
        if (tagIds.isEmpty()) {
            return;
        }
        List<PostTag> postTagList = tagIds.stream().map(tagId -> {
            PostTag postTag = new PostTag();
            postTag.setPostId(postId);
            postTag.setTagId(tagId);
            return postTag;
        }).toList();
        List<BatchResult> batchResultList = postTagMapper.insert(postTagList);
        if (batchResultList.isEmpty() || batchResultList.stream()
                .flatMap(result -> Arrays.stream(result.getUpdateCounts()).boxed())
                .anyMatch(count -> count <= 0)) {
            throw new SystemException(ErrorCode.DB_ERROR, "批量添加帖子标签: " + postTagList + " 失败");
        }
        taskService.setTagData(tagIds.toArray(new Integer[0]));
    }

    @Override
    public void deletePost(String userId, Integer postId) {
        Post post = ensurePostExist(postId);
        if (!post.getUserId().equals(Integer.valueOf(userId))) {
            throw new BizException(ErrorCode.COMMON_PERMISSION_ERROR, "用户ID: " + userId + " 无权删除帖子ID: " + postId);
        }
        if (postMapper.deleteById(postId) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "删除帖子ID: " + postId + " 失败");
        }
        QueryWrapper<PostTag> postTagQueryWrapper = new QueryWrapper<PostTag>().eq("post_id", postId);
        List<Integer> tagIds = postTagMapper.selectObjs(postTagQueryWrapper.select("tag_id"));
        taskService.delTagData(tagIds.toArray(new Integer[0]));
        if (!tagIds.isEmpty() && postTagMapper.delete(postTagQueryWrapper) != tagIds.size()) {
            throw new SystemException(ErrorCode.DB_ERROR, "删除帖子ID: " + postId + " 标签关联失败");
        }
        replyMapper.delete(new QueryWrapper<Reply>().eq("post_id", postId));
        taskService.delPostKey(postId);
    }

    @Override
    public PostVO getPost(String userId, Integer id) {
        Post post = ensurePostExist(id);
        post.setViewCount(post.getViewCount() + 1);
        if (postMapper.updateById(post) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "更新帖子ID: " + id + " 浏览量+1失败");
        }
        Set<Integer> upvotePostIds = getUpvotePostIds(userId);
        Set<Integer> collectPostIds = getCollectPostIds(userId);
        UserDTO userDTO = userFeign.getUserById(post.getUserId());
        return toPostVO(post, List.of(userDTO), upvotePostIds, collectPostIds);
    }

    @Override
    public Pagination<PostVO> getPostListByCategory(String userId, Integer categoryId, Long pageNum, Long pageSize) {
        categoryService.ensureCategoryExist(categoryId);
        QueryWrapper<Post> queryWrapper = new QueryWrapper<Post>()
                .eq("category_id", categoryId)
                .orderByDesc("created_on");
        return getPostList(userId, pageNum, pageSize, queryWrapper);
    }

    @Override
    public Pagination<PostVO> getPostListByTag(String userId, Integer tagId, Long pageNum, Long pageSize) {
        tagService.ensureTagExist(tagId);
        QueryWrapper<PostTag> postTagQueryWrapper = new QueryWrapper<PostTag>()
                .eq("tag_id", tagId);
        Page<PostTag> postTagPage = postTagMapper.selectPage(new Page<>(pageNum, pageSize), postTagQueryWrapper);
        List<Integer> postIds = postTagPage.getRecords().stream().map(PostTag::getPostId).toList();
        if (postIds.isEmpty()) {
            return Pagination.of(Collections.emptyList(), pageNum, pageSize, 0L);
        }
        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<Post>()
                .in("id", postIds);
        Pagination<PostVO> postVOList = getPostList(userId, pageNum, pageSize, postQueryWrapper);
        postVOList.setTotal(postTagPage.getTotal());
        return postVOList;
    }

    @Override
    public Pagination<PostVO> getPostListByUser(String userId, Integer authorId, Long pageNum, Long pageSize) {
        userService.ensureUserExist(String.valueOf(authorId));
        QueryWrapper<Post> queryWrapper = new QueryWrapper<Post>()
                .eq("user_id", authorId)
                .orderByDesc("created_on");
        return getPostList(userId, pageNum, pageSize, queryWrapper);
    }

    @Override
    public Pagination<PostVO> getUpvotePostList(String userId, Long pageNum, Long pageSize) {
        QueryWrapper<Upvote> upvoteQueryWrapper = new QueryWrapper<Upvote>()
                .eq("user_id", userId)
                .orderByDesc("created_on");
        Page<Upvote> upvotePage = upvoteMapper.selectPage(new Page<>(pageNum, pageSize), upvoteQueryWrapper);
        List<Integer> postIds = upvotePage.getRecords().stream().map(Upvote::getPostId).toList();
        if (postIds.isEmpty()) {
            return Pagination.of(Collections.emptyList(), pageNum, pageSize, 0L);
        }
        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<Post>()
                .in("id", postIds);
        Pagination<PostVO> postVOList = getPostList(userId, pageNum, pageSize, postQueryWrapper);
        postVOList.setTotal(upvotePage.getTotal());
        return postVOList;
    }

    @Override
    public Pagination<PostVO> getCollectPostList(String userId, Long pageNum, Long pageSize) {
        QueryWrapper<Collect> collectQueryWrapper = new QueryWrapper<Collect>()
                .eq("user_id", userId)
                .orderByDesc("created_on");
        Page<Collect> collectPage = collectMapper.selectPage(new Page<>(pageNum, pageSize), collectQueryWrapper);
        List<Integer> postIds = collectPage.getRecords().stream().map(Collect::getPostId).toList();
        if (postIds.isEmpty()) {
            return Pagination.of(Collections.emptyList(), pageNum, pageSize, 0L);
        }
        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<Post>()
                .in("id", postIds);
        Pagination<PostVO> postVOList = getPostList(userId, pageNum, pageSize, postQueryWrapper);
        postVOList.setTotal(collectPage.getTotal());
        return postVOList;
    }

    @Override
    public Pagination<PostVO> getHotPostOfDay(String userId, Long pageNum, Long pageSize) {
        Set<Integer> postIds = Optional.ofNullable(
                redisTemplate.opsForZSet().reverseRange(RedisKey.HOT_POST_DAY, (pageNum - 1) * pageSize, pageNum * pageSize - 1)
        ).orElse(new HashSet<>());
        if (postIds.isEmpty()) {
            return Pagination.of(Collections.emptyList(), pageNum, pageSize, 0L);
        }
        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<Post>()
                .in("id", postIds);
        Pagination<PostVO> postVOList = getPostList(userId, pageNum, pageSize, postQueryWrapper);
        postVOList.setTotal(redisTemplate.opsForZSet().zCard(RedisKey.HOT_POST_DAY));
        return postVOList;
    }

    @Override
    public Post ensurePostExist(Integer id) {
        Post post = getById(id);
        if (post == null) {
            throw new BizException(ErrorCode.POST_NOT_FOUND, "帖子ID: " + id + " 不存在");
        }
        return post;
    }

    public Pagination<PostVO> getPostList(String userId, Long pageNum, Long pageSize, QueryWrapper<Post> queryWrapper) {
        IPage<Post> postPage = postMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        List<Post> postList = postPage.getRecords();
        Set<Integer> upvotePostIds = getUpvotePostIds(userId);
        Set<Integer> collectPostIds = getCollectPostIds(userId);
        Set<Integer> userIds = postList.stream().map(Post::getUserId).collect(Collectors.toSet());
        List<UserDTO> userDTOList = userFeign.getUserByIds(userIds.stream().toList());
        List<PostVO> postVOList = postList.stream()
                .map(post -> toPostVO(post, userDTOList, upvotePostIds, collectPostIds))
                .toList();
        return Pagination.of(postVOList, pageNum, pageSize, postPage.getTotal());
    }

    private PostVO toPostVO(Post post, List<UserDTO> userDTOList, Set<Integer> upvotePostIds, Set<Integer> collectPostIds) {
        PostVO postVO = new PostVO();
        BeanUtil.copyProperties(post, postVO);
        UserVO userVO = new UserVO();
        userDTOList.stream().filter(userDTO -> userDTO.getId().equals(post.getUserId())).findFirst().ifPresent(userDTO -> {
            BeanUtil.copyProperties(userDTO, userVO);
        });
        postVO.setUser(userVO);
        postVO.setIsUpvote(upvotePostIds.contains(post.getId()));
        postVO.setIsCollect(collectPostIds.contains(post.getId()));
        List<TagVO> tagVOList;
        List<Integer> tagIds = postTagMapper.selectObjs(new QueryWrapper<PostTag>().eq("post_id", post.getId()).select("tag_id"));
        if (tagIds.isEmpty()) {
            postVO.setTags(Collections.emptyList());
            return postVO;
        }
        tagVOList = tagMapper.selectByIds(tagIds).stream().map(tag -> {
            TagVO tagVO = new TagVO();
            BeanUtil.copyProperties(tag, tagVO);
            return tagVO;
        }).toList();
        postVO.setTags(tagVOList);
        return postVO;
    }

    private Set<Integer> getUpvotePostIds(String userId) {
        return Optional.ofNullable(
                redisTemplate.opsForSet().members(RedisKey.USER_POST_UPVOTE + userId)
        ).orElse(new HashSet<>());
    }

    private Set<Integer> getCollectPostIds(String userId) {
        return Optional.ofNullable(
                redisTemplate.opsForSet().members(RedisKey.USER_COLLECT + userId)
        ).orElse(new HashSet<>());
    }
}