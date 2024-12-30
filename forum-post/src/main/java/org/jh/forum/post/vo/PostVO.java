package org.jh.forum.post.vo;

import lombok.Data;
import org.jh.forum.post.constant.Visibility;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostVO {
    private Integer id;
    private UserVO user;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer upvoteCount;
    private Integer replyCount;
    private Integer collectCount;
    private Integer shareCount;
    private Boolean isCollect;
    private Boolean isUpvote;
    private Visibility visibility;
    private String ip;
    private String ipLoc;
    private LocalDateTime createdOn;
    private List<TagVO> tags;
}