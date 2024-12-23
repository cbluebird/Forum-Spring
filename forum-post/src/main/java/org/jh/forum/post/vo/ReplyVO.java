package org.jh.forum.post.vo;

import lombok.Data;
import org.jh.forum.post.model.Reply;

import java.util.Date;

@Data
public class ReplyVO {
    private Long id;
    private Long postId;
    private Long root;
    private Long parent;
    private Long userId;
    private String content;
    private String ip;
    private String ipLoc;
    private Boolean isEssence;
    private Integer replyCount;
    private Integer thumbsUpCount;
    private Integer thumbsDownCount;
    private Date createdOn;
    private Boolean isUpvote;

    public void setReplyVO(Reply reply) {
        this.id = reply.getId();
        this.postId = reply.getPostId();
        this.root = reply.getRoot();
        this.parent = reply.getParent();
        this.userId = reply.getUserId();
        this.content = reply.getContent();
        this.ip = reply.getIp();
        this.ipLoc = reply.getIpLoc();
        this.isEssence = reply.getIsEssence();
        this.replyCount = reply.getReplyCount();
        this.thumbsUpCount = reply.getThumbsUpCount();
        this.thumbsDownCount = reply.getThumbsDownCount();
        this.createdOn = reply.getCreatedOn();
    }
}
