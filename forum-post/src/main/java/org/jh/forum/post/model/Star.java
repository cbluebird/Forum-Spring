package org.jh.forum.post.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("post_star")
public class Star {
    @TableId
    private Long id;
    private Long postId;
    private Long userId;
    private Date createdOn;
    private Date deletedOn;
}