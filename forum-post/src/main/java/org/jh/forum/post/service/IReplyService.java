package org.jh.forum.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jh.forum.common.api.Pagination;
import org.jh.forum.post.dto.ReplyDTO;
import org.jh.forum.post.model.Reply;
import org.jh.forum.post.vo.ReplyVO;

public interface IReplyService extends IService<Reply> {
    void addReply(String userId, ReplyDTO replyDTO);

    void deleteReply(String userId, Integer replyId);

    Pagination<ReplyVO> getReplyList(String userId, Integer postId);

    Reply ensureReplyExist(Integer id);
}