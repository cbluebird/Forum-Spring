package org.jh.forum.search.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchPostVO {
    private Integer id;
    private UserVO user;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer upvoteCount;
    private Integer replyCount;
    private Integer collectCount;
    private Integer shareCount;
    private String ip;
    private String ipLoc;
    private LocalDateTime modifiedOn;
}