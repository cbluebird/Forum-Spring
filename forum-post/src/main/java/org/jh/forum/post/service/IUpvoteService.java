package org.jh.forum.post.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jh.forum.post.dto.UpvoteDTO;
import org.jh.forum.post.model.Upvote;

public interface IUpvoteService extends IService<Upvote> {
    void upvotePost(String userId, UpvoteDTO upvoteDTO);

    void downvotePost(String userId, UpvoteDTO upvoteDTO);

    void upvoteReply(String userId, UpvoteDTO upvoteDTO);

    void downvoteReply(String userId, UpvoteDTO upvoteDTO);
}