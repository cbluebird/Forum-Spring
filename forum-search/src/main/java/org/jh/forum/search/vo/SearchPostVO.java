package org.jh.forum.search.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchPostVO {
    private Long id;
    private UserVO user;
    private String title;
    private String content;
    private Long viewCount;
    private Long upvoteCount;
    private Long commentCount;
    private Long collectionCount;
    private Long shareCount;
    private String ip;
    private String ipLoc;
    private LocalDateTime modifiedOn;
}