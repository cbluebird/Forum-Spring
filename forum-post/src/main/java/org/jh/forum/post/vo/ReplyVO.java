package org.jh.forum.post.vo;

import lombok.Data;
import org.jh.forum.post.model.Reply;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ReplyVO {
    private Integer id;
    private UserVO user;
    private Integer postId;
    private Integer root;
    private Integer parent;
    private String content;
    private String ip;
    private String ipLoc;
    private Boolean isEssence;
    private Integer upvoteCount;
    private Integer replyCount;
    private LocalDateTime createdOn;
    private Boolean isUpvote;

    public void setReplyVO(Reply reply) {
        this.id = reply.getId();
        this.postId = reply.getPostId();
        this.root = reply.getRoot();
        this.parent = reply.getParent();
        this.content = reply.getContent();
        this.ip = reply.getIp();
        this.ipLoc = reply.getIpLoc();
        this.isEssence = reply.getIsEssence();
        this.upvoteCount = reply.getUpvoteCount();
        this.replyCount = reply.getReplyCount();
        this.createdOn = reply.getCreatedOn();
    }

    public void setUserVO(Map<String, Object> userMap) {
        UserVO userVO = new UserVO();
        userVO.setId((Integer) userMap.get("id"));
        userVO.setUsername((String) userMap.get("username"));
        userVO.setNickname((String) userMap.get("nickname"));
        userVO.setAvatar((String) userMap.get("avatar"));
        this.user = userVO;
    }
}
