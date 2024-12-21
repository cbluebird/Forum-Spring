package org.jh.forum.post.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jh.forum.post.constant.Visibility;

import java.util.Date;

@Data
@TableName("post")
public class Post {
    @TableId
    private Long id;
    private Long userId;
    private Long categoryId;
    private String title;
    private String content;
    private Long commentCount;
    private Long collectionCount;
    private Long upvoteCount;
    private Long viewCount;
    private Long shareCount;
    private Visibility visibility;
    private Integer isTop;
    private Integer isEssence;
    private Integer isSelfHot;
    private String ip;
    private String ipLoc;
    private Date createdOn;
    private Date modifiedOn;
    private Date deletedOn;
}