package org.jh.forum.post.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.common.exception.SystemException;
import org.jh.forum.post.constant.RedisKey;
import org.jh.forum.post.dto.UpvoteDTO;
import org.jh.forum.post.mapper.PostMapper;
import org.jh.forum.post.mapper.ReplyMapper;
import org.jh.forum.post.mapper.UpvoteMapper;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.model.Reply;
import org.jh.forum.post.model.Upvote;
import org.jh.forum.post.service.IPostService;
import org.jh.forum.post.service.IReplyService;
import org.jh.forum.post.service.IUpvoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UpvoteServiceImpl extends ServiceImpl<UpvoteMapper, Upvote> implements IUpvoteService {
    @Autowired
    private IPostService postService;
    @Autowired
    private IReplyService replyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UpvoteMapper upvoteMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;

    @Override
    public void upvotePost(String userId, UpvoteDTO upvoteDTO) {
        Integer postId = upvoteDTO.getId();
        Post post = postService.ensurePostExist(postId);
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(RedisKey.USER_POST_UPVOTE + userId, postId))) {
            throw new BizException(ErrorCode.POST_ALREADY_UPVOTE, "用户ID: " + userId + " 已点赞帖子ID: " + postId);
        }
        Upvote upvote = new Upvote();
        upvote.setPostId(postId);
        upvote.setUserId(Integer.valueOf(userId));
        if (upvoteMapper.insert(upvote) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "添加点赞: " + upvote + " 失败");
        }
        post.setUpvoteCount(post.getUpvoteCount() + 1);
        if (postMapper.updateById(post) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "更新帖子ID: " + postId + " 点赞数+1失败");
        }
        redisTemplate.opsForSet().add(RedisKey.USER_POST_UPVOTE + userId, postId);
        taskService.setPostData(postId);
    }

    @Override
    public void downvotePost(String userId, UpvoteDTO upvoteDTO) {
        Integer postId = upvoteDTO.getId();
        Post post = postService.ensurePostExist(postId);
        if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(RedisKey.USER_POST_UPVOTE + userId, postId))) {
            throw new BizException(ErrorCode.POST_DOWNVOTE, "用户ID: " + userId + " 未点赞帖子ID: " + postId);
        }
        if (upvoteMapper.delete(new QueryWrapper<Upvote>().eq("post_id", postId).eq("user_id", userId)) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "删除用户ID: " + userId + " 点赞帖子ID: " + postId + " 失败");
        }
        post.setUpvoteCount(post.getUpvoteCount() - 1);
        if (postMapper.updateById(post) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "更新帖子ID: " + postId + " 点赞数-1失败");
        }
        redisTemplate.opsForSet().remove(RedisKey.USER_POST_UPVOTE + userId, postId);
        taskService.delPostData(postId);
    }

    @Override
    public void upvoteReply(String userId, UpvoteDTO upvoteDTO) {
        Integer replyId = upvoteDTO.getId();
        Reply reply = replyService.ensureReplyExist(replyId);
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(RedisKey.USER_REPLY_UPVOTE + userId, replyId))) {
            throw new BizException(ErrorCode.POST_ALREADY_UPVOTE, "用户ID: " + userId + " 已点赞回复ID: " + replyId);
        }
        reply.setUpvoteCount(reply.getUpvoteCount() + 1);
        if (replyMapper.updateById(reply) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "更新回复ID: " + replyId + " 点赞数+1失败");
        }
        redisTemplate.opsForSet().add(RedisKey.USER_REPLY_UPVOTE + userId, replyId);
    }

    @Override
    public void downvoteReply(String userId, UpvoteDTO upvoteDTO) {
        Integer replyId = upvoteDTO.getId();
        Reply reply = replyService.ensureReplyExist(replyId);
        if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(RedisKey.USER_REPLY_UPVOTE + userId, replyId))) {
            throw new BizException(ErrorCode.POST_DOWNVOTE, "用户ID: " + userId + " 未点赞回复ID: " + replyId);
        }
        reply.setUpvoteCount(reply.getUpvoteCount() - 1);
        if (replyMapper.updateById(reply) != 1) {
            throw new SystemException(ErrorCode.DB_ERROR, "更新回复ID: " + replyId + " 点赞数-1失败");
        }
        redisTemplate.opsForSet().remove(RedisKey.USER_REPLY_UPVOTE + userId, replyId);
    }
}