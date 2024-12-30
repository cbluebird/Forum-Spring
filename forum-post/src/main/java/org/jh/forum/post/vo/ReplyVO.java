package org.jh.forum.post.vo;

import lombok.Data;

import java.time.LocalDateTime;

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
}