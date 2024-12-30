package org.jh.forum.post.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jh.forum.post.constant.Visibility;

import java.time.LocalDateTime;

@Data
@TableName("post")
public class Post {
    @TableId
    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private String title;
    private String content;
    private Integer viewCount;
    private Integer upvoteCount;
    private Integer replyCount;
    private Integer collectCount;
    private Integer shareCount;
    private Visibility visibility;
    private Boolean isTop;
    private Boolean isEssence;
    private Boolean isSelfTop;
    private String ip;
    private String ipLoc;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private LocalDateTime deletedOn;
}