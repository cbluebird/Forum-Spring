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
    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private String title;
    private String content;
    private Integer commentCount;
    private Integer collectionCount;
    private Integer upvoteCount;
    private Integer viewCount;
    private Integer shareCount;
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