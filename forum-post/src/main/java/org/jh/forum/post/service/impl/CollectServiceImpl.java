package org.jh.forum.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.common.exception.SystemException;
import org.jh.forum.post.constant.RedisKey;
import org.jh.forum.post.dto.CollectDTO;
import org.jh.forum.post.mapper.CollectMapper;
import org.jh.forum.post.mapper.PostMapper;
import org.jh.forum.post.model.Collect;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.service.ICollectService;
import org.jh.forum.post.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements ICollectService {
    @Autowired
    private IPostService postService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @Override
    public void collectPost(String userId, CollectDTO collectDTO) {
        Integer postId = collectDTO.getId();
        Post post = postService.ensurePostExist(postId);
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(RedisKey.USER_COLLECT + userId, postId))) {
            throw new BizException(ErrorCode.POST_ALREADY_COLLECT, "用户ID: " + userId + " 已收藏帖子ID: " + postId);
        }
        Collect collect = new Collect();
        collect.setPostId(postId);
        collect.setUserId(Integer.valueOf(userId));
        if (collectMapper.insert(collect) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "添加收藏: " + collect + " 失败");
        }
        post.setCollectCount(post.getCollectCount() + 1);
        if (postMapper.updateById(post) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "更新帖子ID: " + postId + " 收藏数+1失败");
        }
        redisTemplate.opsForSet().add(RedisKey.USER_COLLECT + userId, postId);
        taskService.setPostData(postId);
    }

    @Override
    public void uncollectPost(String userId, CollectDTO collectDTO) {
        Integer postId = collectDTO.getId();
        Post post = postService.ensurePostExist(postId);
        if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(RedisKey.USER_COLLECT + userId, postId))) {
            throw new BizException(ErrorCode.POST_UNCOLLECT, "用户ID: " + userId + " 未收藏帖子ID: " + postId);
        }
        if (collectMapper.delete(new QueryWrapper<Collect>().eq("post_id", postId).eq("user_id", userId)) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "删除用户ID: " + userId + " 收藏帖子ID: " + postId + " 失败");
        }
        post.setCollectCount(post.getCollectCount() - 1);
        if (postMapper.updateById(post) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "更新帖子ID: " + postId + " 收藏数-1失败");
        }
        redisTemplate.opsForSet().remove(RedisKey.USER_COLLECT + userId, postId);
        taskService.delPostData(postId);
    }
}