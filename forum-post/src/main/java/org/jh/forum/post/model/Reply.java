package org.jh.forum.post.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("reply")
public class Reply {
    @TableId
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
    private Date modifiedOn;
    private Date deletedOn;
}