package org.jh.forum.post.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("reply")
public class Reply {
    @TableId
    private Integer id;
    private Integer postId;
    private Integer root;
    private Integer parent;
    private Integer userId;
    private String content;
    private String ip;
    private String ipLoc;
    private Boolean isEssence;
    private Integer upvoteCount;
    private Integer replyCount;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private LocalDateTime deletedOn;
}