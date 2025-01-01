package org.jh.forum.post.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jh.forum.common.api.ErrorCode;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.common.exception.BizException;
import org.jh.forum.common.exception.SystemException;
import org.jh.forum.post.constant.RedisKey;
import org.jh.forum.post.dto.ReplyDTO;
import org.jh.forum.post.dto.UserDTO;
import org.jh.forum.post.feign.UserFeign;
import org.jh.forum.post.mapper.PostMapper;
import org.jh.forum.post.mapper.ReplyMapper;
import org.jh.forum.post.model.Post;
import org.jh.forum.post.model.Reply;
import org.jh.forum.post.service.IPostService;
import org.jh.forum.post.service.IReplyService;
import org.jh.forum.post.vo.ReplyVO;
import org.jh.forum.post.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements IReplyService {
    @Autowired
    private IPostService postService;
    @Autowired
    private ReplyMapper replyMapper;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    @Autowired
    private UserFeign userFeign;

    @Override
    public void addReply(String userId, ReplyDTO replyDTO) {
        Post post = postService.ensurePostExist(replyDTO.getPostId());
        Reply reply = new Reply();
        BeanUtil.copyProperties(replyDTO, reply);
        reply.setUserId(Integer.valueOf(userId));
        if (replyMapper.insert(reply) != 1) {
            throw new SystemException(ErrorCode.DB_MYSQL_ERROR, "添加回复: " + reply + " 失败");
        }
        post.setReplyCount(post.getReplyCount() + 1);
        if (postMapper.updateById(post) != 1) {
            throw new SystemException(ErrorCode.DB_MYSQL_ERROR, "更新帖子ID: " + post.getId() + " 回复数+1失败");
        }
        if (replyDTO.getRoot() != 0) {
            Reply root = ensureReplyExist(replyDTO.getRoot());
            root.setReplyCount(root.getReplyCount() + 1);
            if (replyMapper.updateById(root) != 1) {
                throw new SystemException(ErrorCode.DB_MYSQL_ERROR, "更新Root回复ID: " + root.getId() + " 回复数+1失败");
            }
        }
        if (replyDTO.getParent() != 0 && !replyDTO.getParent().equals(replyDTO.getRoot())) {
            Reply parent = ensureReplyExist(replyDTO.getParent());
            parent.setReplyCount(parent.getReplyCount() + 1);
            if (replyMapper.updateById(parent) != 1) {
                throw new SystemException(ErrorCode.DB_MYSQL_ERROR, "更新Parent回复ID: " + parent.getId() + " 回复数+1失败");
            }
        }
    }

    @Override
    public void deleteReply(String userId, Integer replyId) {
        Reply reply = ensureReplyExist(replyId);
        if (!reply.getUserId().equals(Integer.valueOf(userId))) {
            throw new BizException(ErrorCode.COMMON_PERMISSION_ERROR, "用户ID: " + userId + " 无权删除回复ID: " + replyId);
        }
        if (reply.getRoot() != 0) {
            Reply root = ensureReplyExist(reply.getRoot());
            root.setReplyCount(root.getReplyCount() - 1);
            if (replyMapper.updateById(root) != 1) {
                throw new SystemException(ErrorCode.DB_MYSQL_ERROR, "更新Root回复ID: " + root.getId() + " 回复数-1失败");
            }
            if (!Objects.equals(reply.getRoot(), reply.getParent()) && reply.getParent() != 0) {
                Reply parent = ensureReplyExist(reply.getParent());
                parent.setReplyCount(parent.getReplyCount() - 1);
                if (replyMapper.updateById(parent) != 1) {
                    throw new SystemException(ErrorCode.DB_MYSQL_ERROR, "更新Parent回复ID: " + parent.getId() + " 回复数-1失败");
                }
            }
        }
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<Reply>()
                .eq("id", replyId)
                .or().eq("root", replyId)
                .or().eq("parent", replyId);
        if (replyMapper.delete(queryWrapper) < 1) {
            throw new SystemException(ErrorCode.DB_MYSQL_ERROR, "删除回复ID: " + replyId + " 及子回复失败");
        }
    }

    @Override
    public Pagination<ReplyVO> getReplyList(String userId, Integer postId) {
        postService.ensurePostExist(postId);
        List<Reply> replyList = replyMapper.selectList(new QueryWrapper<Reply>()
                .eq("post_id", postId)
                .orderByDesc("created_on")
        );
        if (replyList.isEmpty()) {
            return Pagination.of(new ArrayList<>(), 1L, 10L, 0L);
        }
        Set<Integer> upvoteReplyIds = Optional.ofNullable(
                redisTemplate.opsForSet().members(RedisKey.USER_REPLY_UPVOTE + userId)
        ).orElse(new HashSet<>());
        Set<Integer> userIds = replyList.stream().map(Reply::getUserId).collect(Collectors.toSet());
        List<UserDTO> userDTOList = userFeign.getUserByIds(userIds.stream().toList());
        List<ReplyVO> replyVOList = replyList.stream().map(reply -> {
            ReplyVO replyVO = new ReplyVO();
            BeanUtil.copyProperties(reply, replyVO);
            UserVO userVO = new UserVO();
            userDTOList.stream().filter(userDTO -> userDTO.getId().equals(reply.getUserId())).findFirst().ifPresent(userDTO -> {
                BeanUtil.copyProperties(userDTO, userVO);
            });
            replyVO.setUser(userVO);
            replyVO.setIsUpvote(upvoteReplyIds.contains(reply.getId()));
            return replyVO;
        }).toList();
        return Pagination.of(replyVOList, 1L, 10L, (long) replyVOList.size());
    }

    @Override
    public Reply ensureReplyExist(Integer id) {
        Reply reply = replyMapper.selectById(id);
        if (reply == null) {
            throw new BizException(ErrorCode.POST_REPLY_NOT_FOUND, "回复ID: " + id + " 不存在");
        }
        return reply;
    }
}